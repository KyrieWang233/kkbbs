package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.NotificationDto;
import com.kyriewang.kkbbs.enums.NotificationTypeEnum;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotifyController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping("notification/{id}")
    public String notification(@PathVariable("id") Long id,
                               HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDto notificationDto = notificationService.read(id,user);
        if(notificationDto.getType()== NotificationTypeEnum.REPLY_COMMENT.getType()||
                notificationDto.getType()==NotificationTypeEnum.REPLY_QUESTION.getType()){
            return "redirect:/question/"+notificationDto.getOuterid();
        }else{
            return "redirect:/";
        }

    }
}
