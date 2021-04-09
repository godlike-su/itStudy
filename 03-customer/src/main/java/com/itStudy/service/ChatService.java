package com.itStudy.service;

import com.itStudy.entity.Chat;
import com.itStudy.entity.User;

import java.util.List;
import java.util.Map;

public interface ChatService
{
    int saveChat(Chat chat);

    List<Map> unchatShow(int receiveUserId);

    List<User> chatInfo(int sendUserId, int receiveUserId);

    //设置某个用户信息已读
    int chatStatusOne0(int sendUserId, int receiveUserId);

    //查看有多少条信息
    int chatShowRecordCount(int sendUserId, int receiveUserId);

    List<Chat> chatShowRecordList(int sendUserId, int receiveUserId, int startIndex, int pageSize);

}
