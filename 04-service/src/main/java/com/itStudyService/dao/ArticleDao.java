package com.itStudyService.dao;

import com.itStudyService.entity.Article;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface ArticleDao extends Mapper<Article>
{
    List<Map> showArticleAllCount(int startIndex);
}
