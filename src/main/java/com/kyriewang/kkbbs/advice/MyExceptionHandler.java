package com.kyriewang.kkbbs.advice;

import com.alibaba.fastjson.JSON;
import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = ShiroException.class)
    public ResultDto handler(ShiroException ex) {
        log.error("shiro运行时异常：----------------{}", ex);
        return ResultDto.fail(401,ex.getMessage());//401表示shiro异常
    }

    @ResponseBody
    @ExceptionHandler(value = CustomizerException.class)
    public ResultDto handleException(CustomizerException ex){
        log.error("自定义异常：----------------{}", ex);
        return ResultDto.errorOf(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultDto handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：----------------{}", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return ResultDto.fail(objectError.getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultDto handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e);
        return ResultDto.fail(e.getMessage());
    }

    //其他异常
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultDto handleException(Exception ex){
        log.error("其他异常：----------------{}", ex);
        return ResultDto.errorOf(CustomizeErrorCode.SYS_ERROR);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
   /* @ExceptionHandler(value = {Exception.class})
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
    }*/
}
