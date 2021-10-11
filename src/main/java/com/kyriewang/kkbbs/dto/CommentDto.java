package com.kyriewang.kkbbs.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentDto {
    private Long id;
    private Long parent_id;
    private Integer type;
    private Long comment_creator;
    private Long receiver_id;
    private String receiver_name;
    private Long gmt_create;
    private Long gmt_modified;
    private Long like_count;
    private Integer comment_count;
    private String content;
    private UserDto user;
    private Boolean inputShow=false;
    private List<CommentDto> replys;
}
