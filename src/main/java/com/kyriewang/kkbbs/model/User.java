package com.kyriewang.kkbbs.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable{
    private Long id;
    private String account_id;
    private String name;
    private String password;
    private String salt;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;
    private String role;
    private String register_way;//第三方来源
}
