package com.kyriewang.kkbbs.provider;

import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@PropertySource(value = { "classpath:user.properties" }, encoding = "utf-8")
@Component
public class QCloudProvider {
    // 1 初始化用户身份信息（secretId, secretKey）。
    @Value("${qcloud.secretId}")
    private String secretId;
    @Value("${qcloud.secretKey}")
    private String secretKey;

    public String cloudUpload(InputStream fileStream,String mimeType,String filename,String username) {
        String downloadUrl="";

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-nanjing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        String bucketName = "kkbbs-1252824606";
// 指定要上传到 COS 上对象键
        String generatedFileName;
        String[] filePaths = filename.split("\\.");//切分获得文件的属性
        if(filePaths.length>1){
            generatedFileName = UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else{
            throw new CustomizerException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try{
            String key = "picture/"+generatedFileName;
            if(!username.equals("picture")){
                key = "user/"+username+"/"+generatedFileName;
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            //获取输入流的长度
            int streamLength = fileStream.available();
            objectMetadata.setContentLength(streamLength);
            objectMetadata.setContentType(mimeType);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileStream,objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            //生成访问的临时url
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
            // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
            // 这里设置签名在一天后过期
            //这里是两年
            Date expirationDate = new Date(System.currentTimeMillis() + 60L * 60L * 1000L *24*365*2);
            req.setExpiration(expirationDate);
            URL url = cosClient.generatePresignedUrl(req);
            downloadUrl = url.toString();
            System.out.println(downloadUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        //关闭客户端
        cosClient.shutdown();
        return downloadUrl;
    }
    // 用来批量删除
    public void delete(ArrayList<DeleteObjectsRequest.KeyVersion> keyList,String bucketName,COSClient cosClient) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setKeys(keyList);

        // 批量删除文件
        try {
            DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
        } catch (MultiObjectDeleteException mde) { // 如果部分删除成功部分失败, 返回MultiObjectDeleteException
            List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
            List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
        } catch (CosServiceException e) { // 如果是其他错误，例如参数错误， 身份验证不过等会抛出 CosServiceException
            e.printStackTrace();
            throw e;
        } catch (CosClientException e) { // 如果是客户端错误，例如连接不上COS
            e.printStackTrace();
            throw e;
        }
    }

    //删除对应用户的文件夹
    public void deleteFileList(String username){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-nanjing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = "kkbbs-1252824606";

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
// 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
// prefix表示要删除的文件夹
        listObjectsRequest.setPrefix("user/"+username+"/");
// deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
// 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;

        do {
            // 设置要删除的key列表, 最多一次删除1000个
            ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<DeleteObjectsRequest.KeyVersion>();

            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return;
            } catch (CosClientException e) {
                e.printStackTrace();
                return;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                keyList.add(new DeleteObjectsRequest.KeyVersion(key));
            }

            try {
                delete(keyList,bucketName,cosClient);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return;
            } catch (CosClientException e) {
                e.printStackTrace();
                return;
            }

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());

        //关闭客户端
        cosClient.shutdown();

    }
}
