package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.service.NotificationService;
import com.kyriewang.kkbbs.service.QuestionService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @Autowired
    NotificationService notificationService;

    @RequestMapping("profile/{action}")
    public ResultDto profile(HttpServletRequest request,
                             @PathVariable(name="action") String action,
                             @RequestParam(name="page",defaultValue = "1") Integer page,
                             @RequestParam(name="size", defaultValue = "5") Integer size)
    {
        AccountProfile userprofile = null;
        PageDto pageDto = null;
        userprofile = (AccountProfile)SecurityUtils.getSubject().getPrincipal();
        if(userprofile==null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);//抛出没有登录的错误
        }

        if(action.equals("myquestion")){
            pageDto = questionService.getListById(userprofile.getId(),page,size);

        }
        else if(action.equals("message")){
            pageDto = notificationService.getListById(userprofile.getId(),page,size);
        }
        return ResultDto.succ("当前页面信息查询成功",pageDto);
    }
}
