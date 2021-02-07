package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.QuestionMapper;
import com.kyriewang.kkbbs.model.Question;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.TagCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       Model model){
        Question question = questionMapper.getQuestionByquesitonid(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.getDefaultTags());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.getDefaultTags());
        return "publish";
    }

    @PostMapping("/publish")
    public String submitQuestion(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Long id,
            HttpServletRequest request,
            Model model
            ){

        //把数据存储到model当中，这样前端可以拿到数据
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.getDefaultTags());
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("error","问题描述不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setId(id);//这里还是需要id数据，因为与上面的question不是同一个
        if(id==null){//数据库中没有该问题,插入新的数据
            question.setCreator(user.getId());
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            questionMapper.insertQuestion(question);
        }
        else{
            question.setGmt_modified(System.currentTimeMillis());
            Question exit = questionMapper.getQuestionByquesitonid(id);//进行一次校验，防止修改过程中文章被其他人删除
            if(exit==null) throw new CustomizerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            questionMapper.update(question);
        }

        return "redirect:/";
    }
}
