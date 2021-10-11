package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.dto.CommentPostDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.shiro.AccountProfile;

import java.util.List;

public interface CommentService {
    void insert(CommentPostDto commentPostDto, AccountProfile user);

    List<CommentDto> getCommentsByTargetId(Long id, CommentTypeEnum commentTypeEnum);
}
