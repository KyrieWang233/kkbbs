package com.kyriewang.kkbbs.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Question {
    private Long id;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "描述不能为空")
    private String description;
    private Long gmt_create;
    private Long gmt_modified;
    private Long creator;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
    private String tag;

}
