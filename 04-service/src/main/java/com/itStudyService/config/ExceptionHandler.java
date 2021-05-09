package com.itStudyService.config;


import com.itStudyService.spring.AfRestError;
import com.rabbitmq.http.client.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class ExceptionHandler
{
    //未知的错误
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public Object handleHttpException(Exception ex){
        log.error(ex.getMessage());
        return new AfRestError("服务器异常");

    }

    //已知的错误
    @org.springframework.web.bind.annotation.ExceptionHandler(value = HttpException.class)
    public Object handleHttpException(HttpException ex){
        log.error(ex.getMessage());
        return new AfRestError("服务器异常");
    }

    //用户权限不足错误
    @org.springframework.web.bind.annotation.ExceptionHandler(value = UnauthorizedException.class)
    public Object UnauthorizedException(UnauthorizedException ex){
        log.error(ex.getMessage());

        return new AfRestError(403,"权限不足");
    }

}
