package com.itStudy.service.Impl;

import com.itStudy.dao.ChatDao;
import com.itStudy.entity.Chat;
import com.itStudy.entity.User;
import com.itStudy.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService
{
    @Autowired
    private ChatDao chatDao;

    @Override
    public int saveChat(Chat chat)
    {

        return chatDao.insertSelective(chat);
    }

    @Override
    public List<Map> unchatShow(int receiveUserId)
    {
        return chatDao.unchatShow(receiveUserId);
    }

    @Override
    public List<User> chatInfo(int sendUserId, int receiveUserId)
    {
        return chatDao.chatInfo(sendUserId, receiveUserId);
    }

    @Override
    public int chatStatusOne0(int sendUserId, int receiveUserId)
    {
        Example example = new Example(Chat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", sendUserId);
        criteria.andEqualTo("receiveUserId", receiveUserId);

        Chat chat = new Chat();
        chat.setStatus((byte) 0);
        return chatDao.updateByExampleSelective(chat, example);
    }

    @Override
    public int chatShowRecordCount(int sendUserId, int receiveUserId)
    {
        Example example = new Example(Chat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", sendUserId);
        criteria.andEqualTo("receiveUserId", receiveUserId);
        criteria.orEqualTo("sendUserId", receiveUserId);
        criteria.andEqualTo("receiveUserId", sendUserId);

        return chatDao.selectCountByExample(example);
    }

    @Override
    public List<Chat> chatShowRecordList(int sendUserId, int receiveUserId,
                                         int startIndex, int pageSize)
    {
        return chatDao.chatShowRecordList(sendUserId, receiveUserId, startIndex, pageSize);
    }
}
