package com.itStudyService.dao;

import com.itStudyService.entity.UserPerms;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserPermsDao extends Mapper<UserPerms>
{
    int addUserPerms(List<UserPerms> userPermsList);

    int deleteUserPermsByUserId(int userId);
}
