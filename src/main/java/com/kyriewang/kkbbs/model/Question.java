package com.kyriewang.kkbbs.model;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private String title;
    private String description;
    private Long gmt_create;
    private Long gmt_modified;
    private Long creator;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
    private String tag;

}
