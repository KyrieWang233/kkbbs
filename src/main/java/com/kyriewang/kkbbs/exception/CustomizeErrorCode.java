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
    FILE_UPLOAD_FAIL(2010,"图片上传失败")
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
