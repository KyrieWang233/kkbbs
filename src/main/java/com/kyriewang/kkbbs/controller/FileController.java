package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.FileDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.provider.QCloudProvider;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

@RestController
public class FileController {
    @Autowired
    QCloudProvider qCloudProvider;

    @RequiresAuthentication
    @RequestMapping("/file/upload")
    public ResultDto upload(HttpServletRequest request, MultipartFile image){
        MultipartFile file = image;
        Long len = file.getSize();
        double fileSize = (double) len / 1048576;
        if(fileSize>4){
            return ResultDto.errorOf(new CustomizerException(CustomizeErrorCode.UPLOAD_SIZE_ERROR));//文件上传过大返回错误
        }
        try{
            String downloadurl = qCloudProvider.cloudUpload(file.getInputStream(),file.getContentType(),file.getOriginalFilename(),"picture");
            FileDto fileDto = new FileDto();
            fileDto.setSuccess(1);
            fileDto.setMessage("ok");
            fileDto.setUrl(downloadurl);
            return ResultDto.succ("上传成功",fileDto);
        }
        catch (Exception e){
            e.printStackTrace();
            FileDto fileDto = new FileDto();
            fileDto.setSuccess(0);
            fileDto.setMessage("上传失败");
            return ResultDto.succ("上传失败",fileDto);
        }
    }
}
