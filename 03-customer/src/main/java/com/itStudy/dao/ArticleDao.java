package com.itStudy.dao;

import com.itStudy.entity.Article;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ArticleDao extends Mapper<Article>
{

    List<Article> homeArticle(int cat1, Integer startIndex);

    //查看历史文章
    List<Article> historyArticle(List articleIdList);

    //查看自己的文章
    List<Article> myhomeArticle(int myId, int startIndex, int cat1);

    //修改int类型的数据 增加
    int articleUpdateStartAdd(int articleId, String type, int operating);

    //修改int类型的数据 减
    int articleUpdateStartDec(int articleId, String type, int operating);
}
