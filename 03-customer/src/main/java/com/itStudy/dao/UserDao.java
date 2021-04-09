package com.itStudy.dao;

import com.itStudy.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserDao extends Mapper<User>
{
    //根据名查询一个用户
    User findByUserName(String name);

    //根据用户名获取权限与身份
    User findPermsByUserName(String name);

    User findbyArticleRef1(int id);
}
