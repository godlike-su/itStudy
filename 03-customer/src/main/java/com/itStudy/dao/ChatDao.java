package com.itStudy.dao;

import com.itStudy.entity.Chat;
import com.itStudy.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface ChatDao extends Mapper<Chat>
{
    List<Map> unchatShow(int receiveUserId);

    List<User> chatInfo(int sendUserId, int receiveUserId);

    List<Chat> chatShowRecordList(int sendUserId, int receiveUserId, int startIndex, int pageSize);
}
