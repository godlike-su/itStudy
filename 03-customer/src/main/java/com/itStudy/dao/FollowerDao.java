package com.itStudy.dao;

import com.itStudy.entity.Follower;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface FollowerDao extends Mapper<Follower>
{
    //查看我的关注列表
    List<Map> showFollowers(int id, int startIndex, int pageSize);
}
