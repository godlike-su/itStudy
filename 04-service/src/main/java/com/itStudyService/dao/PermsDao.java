package com.itStudyService.dao;

import com.itStudyService.entity.Perms;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PermsDao extends Mapper<Perms>
{
}
