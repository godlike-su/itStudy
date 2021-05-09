package com.itStudyService.service.Impl;

import com.itStudyService.dao.UserPermsDao;
import com.itStudyService.entity.UserPerms;
import com.itStudyService.service.UserPermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPermsServiceImpl implements UserPermsService
{
    @Autowired
    private UserPermsDao userPermsDao;

    @Override
    public int deletePerms(int userId)
    {
        return userPermsDao.deleteUserPermsByUserId(userId);
    }

    @Override
    public int addUserPerms(List<UserPerms> userPermsList)
    {
        return userPermsDao.addUserPerms(userPermsList);
    }
}
