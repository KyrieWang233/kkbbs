package com.kyriewang.kkbbs.mapper;

import com.kyriewang.kkbbs.model.Comment;

public interface CommentExMapper {
    int incCommentCount(Comment comment);
}
