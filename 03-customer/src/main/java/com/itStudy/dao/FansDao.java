package com.itStudy.dao;

import com.itStudy.entity.Fans;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface FansDao extends Mapper<Fans>
{
    int showInterestCount(int m_id);

    //查看粉丝
    List<Map> showFans(int id);

    //互相关注设置
    int SetEachother(int m_id, int o_id, boolean eachOther);
}
