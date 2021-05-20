package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @ResponseBody
    @GetMapping({"/","/index"})
    public ResultDto index(HttpServletRequest request,
                        @RequestParam(name="page", defaultValue = "1") Integer page,
                        @RequestParam(name="size", defaultValue = "5") Integer size){
        PageDto pageDto = questionService.GetQuestionList(page,size);
        return ResultDto.succ("首页查询成功",pageDto);
    }
}
