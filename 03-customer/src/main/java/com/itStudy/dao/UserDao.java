package com.itStudy.dao;

import com.itStudy.entity.User;
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

    User findbyArticleRef1(int id);

    //查看搜索出来的用户
    List<Map> searchUser(String searchContent, int startIndex, int pageSize);
}
