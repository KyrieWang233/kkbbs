package com.kyriewang.kkbbs.dto;

import com.kyriewang.kkbbs.model.User;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long parent_id;
    private Integer type;
    private Long comment_creator;
    private Long gmt_create;
    private Long gmt_modified;
    private Long like_count;
    private Integer comment_count;
    private String content;
    private User user;
}
