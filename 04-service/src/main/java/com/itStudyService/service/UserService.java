package com.itStudyService.service;


import com.itStudyService.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService
{
    //根据学号查询一个用户
    User findByUserStudentID(String StudentID);

    //根据id获取权限与身份
    User findPermsByUserId(int id);

    //获取用户信息数量
    int userHomeCount();

    //获取用户信息
    List<User> userHome(int pageSize, int startIndex);

    //设置用户信息
    int setUser(User user);

    //根据id查找一个用户
    User findByUserId(int id);

    //修改密码
    int updatePassword(int id, String password);

}
