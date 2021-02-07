package com.kyriewang.kkbbs.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String account_id;
    private String name;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;
}
