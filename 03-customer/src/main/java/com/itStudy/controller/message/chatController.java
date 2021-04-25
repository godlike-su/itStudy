package com.itStudy.controller.message;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Chat;
import com.itStudy.entity.User;
import com.itStudy.service.ChatService;
import com.itStudy.spring.AfRestData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天的信息加载
 */
@RestController
@RequestMapping("/user")
public class chatController
{
    @Autowired
    private ChatService chatService;

    //查找个人和他人信息
    @PostMapping("/chat/info.do")
    public Object info(@RequestBody JSONObject jreq)
    {
        //自己id
        int sendUserId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        //他人id
        int receiveUserId = jreq.getInteger("receiveUserId");

        List<User> userList = chatService.chatInfo(sendUserId, receiveUserId);

        return new AfRestData(userList);
    }

    //设置信息状态为0，已读
    @PostMapping("/chatStatusOne0.do")
    public Object chatStatusOne0(@RequestBody JSONObject jreq)
    {
        //自己id
        int receiveUserId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        //他人id
        int sendUserId = jreq.getInteger("receiveUserId");
        int i = chatService.chatStatusOne0(sendUserId, receiveUserId);
        return new AfRestData(i);
    }

    //查看个人聊天信息，暂时设定保存7天
    @PostMapping("/chatShowRecord.do")
    public Object chatShowRecord(@RequestBody JSONObject jreq)
    {
        //自己id
        int sendUserId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        //他人id
        int receiveUserId = jreq.getInteger("receiveUserId");
        int pageNumber = jreq.getInteger("pageNumber");

        //查看有多少条信息
        int count = chatService.chatShowRecordCount(sendUserId, receiveUserId);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Chat> chatList = chatService.chatShowRecordList(sendUserId, receiveUserId, startIndex, pageSize);

        Map map = new HashMap();
        map.put("pageCount", pageCount);
        map.put("chatList", chatList);
        map.put("pageNumber", pageNumber);
        return new AfRestData(map);
    }
}
