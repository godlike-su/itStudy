package com.itStudy.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.itStudy.service.ArticleService;
import com.itStudy.service.StartService;
import com.itStudy.spring.AfRestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class userStartController
{
    @Autowired
    private ArticleService articleService;







}
