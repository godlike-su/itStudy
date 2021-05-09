package com.itStudy.dao;

import com.itStudy.entity.Access_record;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface Access_recordDao extends Mapper<Access_record>
{
}
