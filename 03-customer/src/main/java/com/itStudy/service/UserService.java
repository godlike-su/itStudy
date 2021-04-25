package com.itStudy.service;

import com.itStudy.entity.Follower;
import com.itStudy.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService
{
    //根据名查询一个用户
    User findByUserStudentID(String StudentID);

    //根据id获取权限与身份
    User findPermsByUserId(int id);

    //根据id获取身份信息
    User findByUserId(int id);

    //注册用户
    void register(User user);

    //查看某篇文章，根据作者id来查作者信息
    User findbyArticleRef1(int ref1);

    //根据id查看用户个人信息
    User showInformationByUserid(int id);

    int setInformation(int id, String type, Object value);

    //设置关注信息
    int setInterest(int m_id, int o_id, int follow);

    Follower showFollowerOne(int m_id, int o_id);

    //修改密码
    int updatePassword(int id, String password);

    //查看搜索用户的数量
    int searchUserCount(String searchContent);

    //查看搜索出来的用户
    List<Map> searchUser(String searchContent, int startIndex, int pageSize);
}
