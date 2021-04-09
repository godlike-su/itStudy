package com.itStudy.service.Impl;

import com.itStudy.dao.FollowerDao;
import com.itStudy.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FollowerServiceImpl implements FollowerService
{
    @Autowired
    private FollowerDao followerDao;

    @Override
    public List<Map> showFollowers(int id)
    {
        return followerDao.showFollowers(id);
    }
}
