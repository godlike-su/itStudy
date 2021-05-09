package com.itStudyService.dao;

import com.itStudyService.entity.Access_record;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface Access_recordDao extends Mapper<Access_record>
{
    List<Map> showTimeSort();
}
