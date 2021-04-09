package com.itStudy.dao;

import com.itStudy.entity.UserPerms;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserPermsDao extends Mapper<UserPerms>
{

}
