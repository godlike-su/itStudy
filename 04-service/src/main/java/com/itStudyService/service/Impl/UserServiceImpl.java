package com.itStudyService.service.Impl;


import com.itStudyService.dao.UserDao;
import com.itStudyService.entity.User;
import com.itStudyService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;



    @Override
    public User findByUserStudentID(String StudentID)
    {
        try
        {
            return userDao.findByUserStudentID(StudentID);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public User findPermsByUserId(int id)
    {
        return userDao.findPermsByUserId(id);
    }

    @Override
    public int userHomeCount()
    {
        return userDao.userHomeCount();
    }

    @Override
    public List<User> userHome(int pageSize, int startIndex)
    {
        return userDao.userHome(pageSize, startIndex);
    }

    @Override
    public int setUser(User user)
    {
        return userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public User findByUserId(int id)
    {
        User user = new User();
        user.setId(id);
        return userDao.selectByPrimaryKey(user);
    }

    @Override
    public int updatePassword(int id, String password)
    {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        return userDao.updateByPrimaryKeySelective(user);
    }
}
