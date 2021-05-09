package com.itStudyService.service.Impl;
import com.itStudyService.dao.Access_recordDao;
import com.itStudyService.entity.Access_record;
import com.itStudyService.service.Access_recordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Access_recordServiceImpl implements Access_recordService
{
    @Autowired
    private Access_recordDao access_recordDao;


    @Override
    public int addAccess(Access_record access_record)
    {
        return access_recordDao.insertSelective(access_record);
    }

    @Override
    public int AccessAllCount()
    {
        return access_recordDao.selectCountByExample(null);
    }

    @Override
    public List<Map> showTimeSort()
    {
        return access_recordDao.showTimeSort();
    }

    @Override
    public int showTypeAccess(String role)
    {
        Access_record a = new Access_record();
        a.setRole(role);
        return access_recordDao.selectCount(a);
    }
}
