package com.itStudy.service.Impl;

import com.itStudy.dao.FollowerDao;
import com.itStudy.dao.FansDao;
import com.itStudy.dao.UserDao;
import com.itStudy.dao.UserPermsDao;
import com.itStudy.entity.*;
import com.itStudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPermsDao userPermsDao;

    @Autowired
    private FansDao fansDao;

    @Autowired
    private FollowerDao followerDao;

    @Override
    public User findByUserName(String name)
    {
        try
        {
            return userDao.findByUserName(name);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public User findPermsByUserName(String name)
    {
        return userDao.findPermsByUserName(name);
    }

    @Override
    public void register(User user)
    {
        try
        {
            //tk插入用户
            userDao.insertSelective(user);
            //用户shiro身份权限注册
            for (int i = 1; i < 4; i++)
            {
                UserPerms userPerms = new UserPerms();
                userPerms.setUserId(user.getId());
                userPerms.setPermsId(i);
                userPermsDao.insertSelective(userPerms);
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.getMessage();
        }
    }

    @Override
    public User findbyArticleRef1(int ref1)
    {
        return userDao.findbyArticleRef1(ref1);
    }

    @Override
    public User showInformationByUserid(int id)
    {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int setInformation(int id, String type, Object value)
    {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        User user = new User();
        user.setId(id);
        if ("name".equals(type))
        {
            user.setName((String) value);
        } else if ("sex".equals(type))
        {
            user.setSex((Boolean) value);
        } else if ("birthday".equals(type))
        {
            user.setBirthday((Date) value);
        } else if ("introduction".equals(type))
        {
            user.setIntroduction((String) value);
        } else if ("school".equals(type))
        {
            user.setSchool((String) value);
        } else if ("studentID".equals(type))
        {
            user.setStudentID((String) value);
        } else if ("profession".equals(type))
        {
            user.setProfession((String) value);
        } else if ("phone".equals(type))
        {
            user.setPhone((String) value);
        } else if ("email".equals(type))
        {
            user.setEmail((String) value);
        }

//        System.out.println(value);
//        userDao.updateByExampleSelective(user, example);
        return userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public int setInterest(int m_id, int o_id, int follow)
    {
        //关注
        Follower follower = new Follower();
        follower.setM_id(m_id);
        follower.setO_id(o_id);

        Fans fans = new Fans();
        fans.setM_id(o_id);
        fans.setO_id(m_id);

        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        int i = 0;

        try
        {
            if (follow == 0)
            {
                follower.setCreateTime(new Date());
                fans.setCreateTime(new Date());
                i = fansDao.insertSelective(fans);
                followerDao.insertSelective(follower);
                int j = fansDao.SetEachother(m_id, o_id, true);
                if(j<2)
                {
                    fansDao.SetEachother(m_id, o_id, false);
                }

            }
            //取消关注
            else if (follow == 1)
            {
                i = fansDao.deleteByPrimaryKey(fans);
                followerDao.deleteByPrimaryKey(follower);
                fansDao.SetEachother(m_id, o_id, false);
            } else
            {
                return 0;
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return i;
    }

    @Override
    public Follower showFollowerOne(int m_id, int o_id)
    {
        Follower follower = new Follower();
        follower.setM_id(m_id);
        follower.setO_id(o_id);
        follower = followerDao.selectByPrimaryKey(follower);
        return follower;
    }
}
