package com.itStudy.dao;

import com.itStudy.entity.Article;
import com.itStudy.entity.Start;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface StartDao extends Mapper<Start>
{
    //查看我的关注列表
    List<Article> homeStart(int userId, int startIndex);

}
