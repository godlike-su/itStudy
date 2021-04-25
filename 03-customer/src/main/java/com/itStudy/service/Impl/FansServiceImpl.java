package com.itStudy.service.Impl;

import com.itStudy.dao.FansDao;
import com.itStudy.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FansServiceImpl implements FansService
{
    @Autowired
    private FansDao fansDao;

    @Override
    public int showInterestCount(int m_id)
    {
        return fansDao.showInterestCount(m_id);
    }

    @Override
    public List<Map> showFans(int id, int startIndex, int pageSize)
    {
        return fansDao.showFans(id, startIndex, pageSize);
    }
}
