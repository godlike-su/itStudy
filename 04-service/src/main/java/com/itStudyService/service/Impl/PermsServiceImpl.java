package com.itStudyService.service.Impl;

import com.itStudyService.dao.PermsDao;
import com.itStudyService.entity.Perms;
import com.itStudyService.service.PermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermsServiceImpl implements PermsService
{
    @Autowired
    private PermsDao permsDao;

    @Override
    public List<Perms> showPerms()
    {
        return permsDao.selectAll();
    }
}
