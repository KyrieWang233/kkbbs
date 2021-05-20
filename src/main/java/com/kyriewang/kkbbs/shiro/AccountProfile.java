package com.kyriewang.kkbbs.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {
    private Long id;
    private String name;
    private String avatar_url;
    private String role;
    private String register_way;//第三方来源
    private Long unreadCount;
}
