package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.dto.CommentPostDto;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.model.Comment;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.CommentService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentPostDto commentPostDto, HttpServletRequest request){

        //从shiro里拿取用户信息，根据用户信息插入
        AccountProfile userprofile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if(userprofile==null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentPostDto==null||commentPostDto.getContent()==null||commentPostDto.getContent().equals("")){
            return ResultDto.errorOf(CustomizeErrorCode.COMMENT_EMPTY_ERROR);
        }

        commentService.insert(commentPostDto,userprofile);
        return ResultDto.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public Object post(@PathVariable("id") Long id){
        List<CommentDto> commentDtos = commentService.getCommentsByTargetId(id, CommentTypeEnum.COMMENT);
        return commentDtos;
    }
}
