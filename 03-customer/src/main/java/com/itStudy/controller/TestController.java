package com.itStudy.controller;

import com.itStudy.spring.AfRestData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController
{
    @PostMapping("/isLogin")
    public Object isLogin()
    {
        return new AfRestData("");
    }
}
