package com.itStudy.config;

import com.itStudy.spring.AfRestError;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class ExceptionHandler
{
    //未知的错误
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public Object handleHttpException(Exception ex){
        log.error(ex.getMessage());
//        System.out.println(ex.getMessage());
        return new AfRestError("服务器异常");

    }

    //已知的错误
    @org.springframework.web.bind.annotation.ExceptionHandler(value = HttpException.class)
    public Object handleHttpException(HttpException ex){
        log.error(ex.getMessage());
//        System.out.println(ex.getMessage());
        return new AfRestError("服务器异常");
    }
}
