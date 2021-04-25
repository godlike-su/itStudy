package com.itStudy.service.Impl;

import com.itStudy.dao.FollowerDao;
import com.itStudy.entity.Follower;
import com.itStudy.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FollowerServiceImpl implements FollowerService
{
    @Autowired
    private FollowerDao followerDao;

    @Override
    public int showFollowersCount(int id)
    {
        //查询总数的筛选条件
        Example example = new Example(Follower.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("m_id", id);    //
        return followerDao.selectCountByExample(example);
    }

    @Override
    public List<Map> showFollowers(int id, int startIndex, int pageSize)
    {
        return followerDao.showFollowers(id, startIndex, pageSize);
    }
}
