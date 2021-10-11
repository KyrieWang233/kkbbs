package com.kyriewang.kkbbs.shiro;

import lombok.Data;

import java.io.Serializable;

//当前登陆的信息
@Data
public class AccountProfile implements Serializable {
    private Long id;
    private String name;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;
    private String role;
    private String register_way;//第三方来源
    private Long unreadCount;
}
