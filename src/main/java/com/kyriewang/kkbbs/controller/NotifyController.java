package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.NotificationDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.enums.NotificationTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.NotificationService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class NotifyController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping("notification/{id}")
    public ResultDto notification(@PathVariable("id") Long id,
                               HttpServletRequest request){
        //从shiro里拿取用户信息，根据用户信息插入
        AccountProfile userprofile = (AccountProfile)SecurityUtils.getSubject().getSession().getAttribute("userprofile");
        if(userprofile==null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);//抛出没有登录的错误，这个需要被前端捕获
        }
        //修改对应userid的通知状态
        if(notificationService.read(id,userprofile.getId())){
            System.out.println(userprofile.getUnreadCount());
            userprofile.setUnreadCount(userprofile.getUnreadCount()-1);
            SecurityUtils.getSubject().getSession().setAttribute("userprofile",userprofile);
            return ResultDto.succ("查看消息成功！",userprofile);
        }
        else{
            return ResultDto.errorOf(CustomizeErrorCode.NOTIFICATION_HAS_READ);//这个好像也可以捕获，前端得先做限制。
        }

    }

    //写一个更新未读消息的接口，用来前端查询
    @GetMapping("unreadCount")
    public ResultDto getUnreadCount(){
        //从shiro认证里拿取用户信息
        AccountProfile userprofile = (AccountProfile)SecurityUtils.getSubject().getSession().getAttribute("userprofile");
        if(userprofile==null){
            return ResultDto.succ("用户未登陆",null);//这里知识查询一个信息，不需要报错
        }
        Long unreadCount = notificationService.getUnreadCount(userprofile.getId());
        userprofile.setUnreadCount(unreadCount);
        SecurityUtils.getSubject().getSession().setAttribute("userprofile",userprofile);//更新过后存入session
        return ResultDto.succ("查询登录信息成功！",userprofile);
    }
}
