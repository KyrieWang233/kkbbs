package com.kyriewang.kkbbs.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private Long id;
    private Long notifier;
    private Long outerid;
    private Integer type;
    private String typename;
    private Long gmt_create;
    private Integer status;
    private String out_title;
    private String out_username;
}
