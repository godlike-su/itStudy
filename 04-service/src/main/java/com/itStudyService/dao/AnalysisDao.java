package com.itStudyService.dao;

import com.itStudyService.entity.Analysis;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AnalysisDao extends Mapper<Analysis>
{
}
