package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) throws Throwable {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            //获取当前request当中的原始异常，然后再抛出。这样可以针对不同的异常进行特殊处理。
            javax.servlet.ServletException servletException = (javax.servlet.ServletException)request.getAttribute("javax.servlet.error.exception");
            throw servletException.getRootCause();
            //该属性给出的异常产生的信息，可存储为java.lang.Throwable
        }
    }

    @RequestMapping("/filterError")
    public void handleFilterError(HttpServletRequest request){
        CustomizeErrorCode errorCode = (CustomizeErrorCode) request.getAttribute("code");
        throw new CustomizerException(errorCode);
    }
}
