package com.itStudyService.service.Impl;

import com.itStudyService.dao.NavtabDao;
import com.itStudyService.entity.Navtab;
import com.itStudyService.service.NavtabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NavtabServiceImpl implements NavtabService
{
    @Autowired
    private NavtabDao navtabDao;

    @Override
    public int updateNavTab(Navtab navtab)
    {
        return navtabDao.updateByPrimaryKeySelective(navtab);
    }

    @Override
    public int addNavtab(Navtab navtab)
    {
        return navtabDao.insertSelective(navtab);
    }
}
