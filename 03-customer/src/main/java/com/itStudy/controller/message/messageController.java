package com.itStudy.controller.message;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Fans;
import com.itStudy.entity.User;
import com.itStudy.service.ChatService;
import com.itStudy.service.FansService;
import com.itStudy.spring.AfRestData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 信息首页的操控
 */
@RestController
@RequestMapping("/user")
public class messageController
{
    @Autowired
    private ChatService chatService;

    @Autowired
    private FansService fansService;

    //message首页聊天
    @PostMapping("/chatIndex.do")
    public Object chatIndex(@RequestBody JSONObject jreq)
    {
        int receiveUserId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        //查看未读信息
        List<Map> map = chatService.unchatShow(receiveUserId);

        return new AfRestData(map);
    }




}
