package com.kyriewang.kkbbs.dto;

import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultDto implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public static ResultDto errorOf(Integer code,String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setData(null);
        resultDto.setMessage(message);
        return resultDto;
    }

    //根据异常返回错误
    public static ResultDto errorOf(CustomizerException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }

    //根据错误代码返回结果
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
    public static ResultDto succ(String mess, Object data) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setData(data);
        resultDto.setMessage(mess);
        return resultDto;
    }
    public static ResultDto fail(String mess){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(-1);
        resultDto.setData(null);
        resultDto.setMessage(mess);
        return resultDto;
    }
    public static ResultDto fail(int code,String mess){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(mess);
        resultDto.setData(null);
        return resultDto;
    }
}
