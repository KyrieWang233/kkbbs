package com.kyriewang.kkbbs.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//对用户进行一层封装，防止泄露信息
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;
    private String role;
    private String register_way;//第三方来源
}
