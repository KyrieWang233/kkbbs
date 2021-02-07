package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.dto.CommentPostDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.model.Comment;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentPostDto commentPostDto, HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentPostDto==null||commentPostDto.getContent()==null||commentPostDto.getContent().equals("")){
            return ResultDto.errorOf(CustomizeErrorCode.COMMENT_EMPTY_ERROR);
        }
        Comment comment = new Comment();
        comment.setParent_id(commentPostDto.getParent_id());
        comment.setContent(commentPostDto.getContent());
        comment.setType(commentPostDto.getType());
        comment.setComment_creator(user.getId());
        comment.setComment_count(0);
        comment.setLike_count(0l);
        comment.setGmt_create(System.currentTimeMillis());
        comment.setGmt_modified(System.currentTimeMillis());
        commentService.insert(comment,user);
        return ResultDto.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public Object post(@PathVariable("id") Long id){
        List<CommentDto> commentDtos = commentService.getCommentsByTargetId(id, CommentTypeEnum.COMMENT);
        return commentDtos;
    }
}
