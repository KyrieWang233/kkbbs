package com.kyriewang.kkbbs.exception;

public class CustomizerException extends RuntimeException{
    private Integer code;
    private String message;

    public CustomizerException(CustomizeErrorCode customizeErrorCode) {
        this.message = customizeErrorCode.getMessage();
        this.code = customizeErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
