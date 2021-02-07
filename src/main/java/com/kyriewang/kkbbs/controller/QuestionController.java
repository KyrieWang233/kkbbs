package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.dto.QuestionDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.model.Question;
import com.kyriewang.kkbbs.service.CommentService;
import com.kyriewang.kkbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @RequestMapping("question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model){
        QuestionDto questionDto = questionService.getQuestionById(id);
        questionService.incView(id);
        List<CommentDto> commentDtos = commentService.getCommentsByTargetId(id, CommentTypeEnum.QUESSTION);
        List<Question> relatedQuestions = questionService.getRelatedQuestions(questionDto);
        questionDto.setView_count(questionDto.getView_count()+1);
        model.addAttribute("question",questionDto);
        model.addAttribute("comments",commentDtos);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
