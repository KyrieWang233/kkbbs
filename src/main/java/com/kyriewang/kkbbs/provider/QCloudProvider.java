package com.kyriewang.kkbbs.provider;

import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
public class QCloudProvider {
    // 1 初始化用户身份信息（secretId, secretKey）。
    String secretId = "AKIDhBOL0RYU6VkkCNsKhpjPoBRbUa1CBx1o";
    String secretKey = "e2E6RFgvyluoUt1uaNwKVCes1vd6LNO8";

    public String cloudUpload(InputStream fileStream,String mimeType,String filename) {
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
            // 这里设置签名在一个小时后过期
            Date expirationDate = new Date(System.currentTimeMillis() + 60L * 60L * 1000L *24);
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
}
