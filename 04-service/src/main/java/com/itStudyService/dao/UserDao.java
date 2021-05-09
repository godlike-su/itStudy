package com.itStudyService.dao;

import com.itStudyService.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao extends Mapper<User>
{
    //根据名查询一个用户
    User findByUserStudentID(String studentID);

    //根据用户名获取权限与身份
    User findPermsByUserId(int id);

    //获取用户信息
    int userHomeCount();

    //获取用户信息
    List<User> userHome(int pageSize, int startIndex);

    List<Map> authorityShow(int pageSize, int startIndex);
}
