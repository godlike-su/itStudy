package com.itStudy.service.Impl;

import com.itStudy.dao.Access_recordDao;
import com.itStudy.entity.Access_record;
import com.itStudy.service.Access_recordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
