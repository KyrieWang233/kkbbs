package com.kyriewang.kkbbs.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找的问题不存在了，要不换个问题试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"请先登录账户"),
    SYS_ERROR(2004,"服务器冒烟了，要不你稍后再试试"),
    COMMENT_TYPE_ERROR(2005,"评论类型错误或者不存在"),
    COMMENT_NOT_FOUND(2006,"抱歉，你找的评论不存在了" ),
    COMMENT_EMPTY_ERROR(2007,"评论不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    TOKEN_TIMEOUT_ERROR(2011,"token已失效，请重新登录"),
    NOTIFICATION_HAS_READ(2012,"这个消息已经被你读过了"),
    PASSWORD_ERROR(2013,"密码错误！"),
    USER_NOT_FOUND(2014,"用户不存在！"),
    LOGIN_TIME_OUT(2015,"登录超时！"),
    UPLOAD_SIZE_ERROR(2016,"上传文件过大！"),
    DELETE_QUESTION_ERROR(2017,"不能删除别人的问题")
    ;

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode(){return code;}
}
