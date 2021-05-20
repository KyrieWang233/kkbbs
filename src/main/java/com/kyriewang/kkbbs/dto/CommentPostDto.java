package com.kyriewang.kkbbs.dto;

import lombok.Data;

//用来接受评论json数据的传输模型
@Data
public class CommentPostDto {
    private Long parent_id;
    private String content;
    private Integer type;
    private Long receiver_id;//如果是二级评论就会带有这个回复id
}
