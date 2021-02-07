package com.kyriewang.kkbbs.dto;

import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import lombok.Data;

@Data
public class ResultDto {
    private Integer code;
    private String message;

    public static ResultDto errorOf(Integer code,String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizerException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }

    public static ResultDto errorOf(CustomizeErrorCode sysError) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(sysError.getCode());
        resultDto.setMessage(sysError.getMessage());
        return resultDto;
    }

    public static ResultDto okOf(){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("评论成功");
        return resultDto;
    }
}
