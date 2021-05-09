package com.itStudyService.service;

import com.itStudyService.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService
{
    //查看所有文章数据数量
    int showArticleAllCount();

    List<Map> showArticleAllCount(int startIndex);

    //设置审核信息等
    int updateArticle(Article article);



}
