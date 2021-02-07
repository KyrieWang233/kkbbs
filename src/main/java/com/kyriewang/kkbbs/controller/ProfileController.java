package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.NotificationService;
import com.kyriewang.kkbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @Autowired
    NotificationService notificationService;

    @RequestMapping("profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name="action") String action,
                          Model model,
                          @RequestParam(name="page",defaultValue = "1") Integer page,
                          @RequestParam(name="size", defaultValue = "5") Integer size)
    {
        User user = null;
        PageDto pageDto = null;
        user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }

        if(action.equals("question")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
            pageDto = questionService.getListById(user.getId(),page,size);
            model.addAttribute("questions",pageDto.getQuestionDtos());

        }
        else if(action.equals("replies")){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","新的回复");
            pageDto = notificationService.getListById(user.getId(),page,size);
            model.addAttribute("notifications",pageDto.getNotificationDtos());
        }
        model.addAttribute("currentpage",page);
        model.addAttribute("totalpages",pageDto.getTotalPages());
        return "profile";
    }
}
