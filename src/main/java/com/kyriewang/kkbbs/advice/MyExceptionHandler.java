package com.kyriewang.kkbbs.advice;

import com.alibaba.fastjson.JSON;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView  handleControllerException(HttpServletRequest request, HttpServletResponse response, Throwable ex, Model model) {
        if(request.getContentType().equals("application/json")){
            ResultDto resultDto;
            if(ex instanceof CustomizerException) {
                resultDto = ResultDto.errorOf((CustomizerException) ex);
            }else {
                resultDto = ResultDto.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try{
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            return null;
        }
        else{
            if(ex instanceof CustomizerException){
                model.addAttribute("message",ex.getMessage());
            }else {
                model.addAttribute("message", "服务器冒烟了，要不你稍后再试试");
            }
            return new ModelAndView("error");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
