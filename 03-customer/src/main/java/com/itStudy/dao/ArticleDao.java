package com.itStudy.dao;

import com.itStudy.entity.Article;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface ArticleDao extends Mapper<Article>
{

    List<Map> homeArticle(int cat1, Integer startIndex);

    List<Map> homeArticleHold(Integer startIndex);

    //查看历史文章
    List<Map> historyArticle(List articleIdList);

    //查看自己的文章
    List<Map> myhomeArticle(int myId, int startIndex, int cat1);

    //修改int类型的数据 增加
    int articleUpdateStartAdd(int articleId, String type, int operating);

    //修改int类型的数据 减
    int articleUpdateStartDec(int articleId, String type, int operating);

    //查看他人首页文章简略加载
    List<Map> otherHomeArticle(int userId, int startIndex);
}
