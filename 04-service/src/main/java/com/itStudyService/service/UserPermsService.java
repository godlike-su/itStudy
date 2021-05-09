package com.itStudyService.service;

import com.itStudyService.entity.UserPerms;

import java.util.List;

public interface UserPermsService
{
    //删除权限与用户表的关联
    int deletePerms(int userId);

    //增加权限表
    int addUserPerms(List<UserPerms> userPermsList);
}
