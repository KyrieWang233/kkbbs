package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.cache.HotTagCache;
import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @ResponseBody
    @GetMapping({"/","/index"})
    public ResultDto index(HttpServletRequest request,
                        @RequestParam(name="page", defaultValue = "1") Integer page,
                        @RequestParam(name="size", defaultValue = "5") Integer size,
                           @RequestParam(name="search",required = false) String search,
                           @RequestParam(name = "tag",required = false) String tag,
                           @RequestParam(name = "sort",required = false) String sort){
        PageDto pageDto = questionService.GetQuestionList(search,tag,sort,page,size);
        return ResultDto.succ("首页查询成功",pageDto);
    }

    @ResponseBody
    @GetMapping("/hottags")
    public ResultDto hotTags(HttpServletRequest request){
        List<String> hots = HotTagCache.getHotTagCache().getHots();
        return ResultDto.succ("热门标签查询成功！",hots);
    }
}
