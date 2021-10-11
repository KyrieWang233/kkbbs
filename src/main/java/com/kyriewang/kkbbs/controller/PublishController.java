package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.QuestionMapper;
import com.kyriewang.kkbbs.model.Question;
import com.kyriewang.kkbbs.service.TagCache;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import io.jsonwebtoken.lang.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/publish/{id}")
    public ResultDto edit(@PathVariable("id") Long id,
                          Model model){
        Question question = questionMapper.getQuestionByquesitonid(id);
        if(question==null){
            Assert.notNull(question, "该问题不存在了!");
        }
        model.addAttribute("question",question);
        model.addAttribute("tags",TagCache.getDefaultTags());
        return ResultDto.succ("问题查询成功",model);
    }

    //返回标签数据
    @GetMapping("/publish/add")
    public ResultDto publish(){
        return ResultDto.succ("tags",TagCache.getDefaultTags());
    }

    /*@RequestParam(value = "title",required = false) String title,
    @RequestParam(value = "description",required = false) String description,
    @RequestParam(value = "tag",required = false) String tag,
    @RequestParam(value = "id",required = false) Long id,*/

    @RequiresAuthentication
    @PostMapping("/publish/add")
    public ResultDto submitQuestion(
            @Validated @RequestBody Question questiontemp
            ){

        //从shiro里拿取用户信息
        AccountProfile userprofile = (AccountProfile)SecurityUtils.getSubject().getPrincipal();
        if(userprofile==null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Long id = questiontemp.getId();
        Question question = new Question();
        question.setTitle(questiontemp.getTitle());
        question.setDescription(questiontemp.getDescription());
        question.setTag(questiontemp.getTag());
        question.setId(id);//这里还是需要id数据，因为与上面的question不是同一个
        if(id==null){//数据库中没有该问题,插入新的数据
            question.setCreator(userprofile.getId());
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

        return ResultDto.succ("提交成功",null);
    }
}
