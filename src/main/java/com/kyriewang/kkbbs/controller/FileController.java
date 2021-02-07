package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.FileDto;
import com.kyriewang.kkbbs.provider.QCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

@Controller
public class FileController {
    @Autowired
    QCloudProvider qCloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDto upload(HttpServletRequest request){

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try{
            String downloadurl = qCloudProvider.cloudUpload(file.getInputStream(),file.getContentType(),file.getOriginalFilename());
            FileDto fileDto = new FileDto();
            fileDto.setSuccess(1);
            fileDto.setMessage("ok");
            fileDto.setUrl(downloadurl);
            return fileDto;
        }
        catch (Exception e){
            e.printStackTrace();
            FileDto fileDto = new FileDto();
            fileDto.setSuccess(0);
            fileDto.setMessage("上传失败");
            return fileDto;
        }
    }
}
