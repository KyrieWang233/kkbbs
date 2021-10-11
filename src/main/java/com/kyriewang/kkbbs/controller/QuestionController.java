package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.dto.QuestionDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.model.Question;
import com.kyriewang.kkbbs.service.CommentService;
import com.kyriewang.kkbbs.service.QuestionService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @GetMapping("question/{id}")
    public ResultDto question(@PathVariable("id") Long id,
                           Model model){
        QuestionDto questionDto = questionService.getQuestionById(id);
        questionService.incView(id);
        List<CommentDto> commentDtos = commentService.getCommentsByTargetId(id, CommentTypeEnum.QUESSTION);
        List<Question> relatedQuestions = questionService.getRelatedQuestions(questionDto);
        questionDto.setView_count(questionDto.getView_count()+1);
        model.addAttribute("question",questionDto);
        model.addAttribute("comments",commentDtos);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return ResultDto.succ("查询成功",model);
    }

    @DeleteMapping("question/{id}")
    public ResultDto delQuestion(@PathVariable("id") Long id){
        QuestionDto questionDto = questionService.getQuestionById(id);
        AccountProfile userprofile = (AccountProfile) SecurityUtils.getSubject().getSession().getAttribute("userprofile");
        //Long类型判断等于不用双等于
        if(!userprofile.getId().equals(questionDto.getCreator())){//如果当前用户不是问题的创建者，那么不能删除
            throw new CustomizerException(CustomizeErrorCode.DELETE_QUESTION_ERROR);
        }
        questionService.deleteQuestion(id);
        return ResultDto.succ("删除问题成功",null);
    }
}
