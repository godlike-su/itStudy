package com.itStudy.service.Impl;

import com.itStudy.dao.StartDao;
import com.itStudy.entity.Start;
import com.itStudy.service.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StartServiceImpl implements StartService
{
    @Autowired
    private StartDao startDao;

    @Override
    public Map showStartOne(int userId, int aId, int startType)
    {
        Example example = new Example(Start.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("aId", aId);
        criteria.andEqualTo("startType", startType);
        Map map = new HashMap();
        map.put("start", startDao.selectOneByExample(example));
        return map;
    }

    @Override
    public int insertStart(int userId, int aId, int startType)
    {
        Start start = new Start();
        start.setUserId(userId);
        start.setAId(aId);
        start.setStartType(startType);
        start.setCreateTime(new Date());
        return startDao.insertSelective(start);
    }

    @Override
    public int deletStart(int userId, int aId, int startType)
    {
        Example example = new Example(Start.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("aId", aId);
        criteria.andEqualTo("startType", startType);
        return startDao.deleteByExample(example);
    }

    @Override
    public int startCount(int userId, int startType)
    {
        Start start = new Start();
        start.setUserId(userId);
        start.setStartType(startType);
        return startDao.selectCount(start);
    }

    @Override
    public List<Map> homeStart(int userId, int startIndex)
    {
        return startDao.homeStart(userId, startIndex);
    }

    @Override
    public List<Map> myStartAnalysisList(int userId, int startIndex)
    {
        return startDao.myStartAnalysisList(userId, startIndex);
    }
}
