package com.itStudy.service;

import com.itStudy.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService
{
    //插入数据到文章
    int insertSelective(Article article);

    //修改文章
    int updateArticle(Article article);

    //首页文章简略加载
    List<Map> homeArticle(int cat1, Integer pageNumber);

    //首页热榜文章简略加载
    List<Map> homeArticleHold(Integer pageNumber);

    //首页文章符合记录的数量
    int homeArticleCount(int cat1);

    //查看热榜的文章数量
    int homeArticleHoldCount();

    //用户查看某篇文章
    Article showOneArticle(int id, int cat1);

    //查看历史文章
    List<Map> historyArticle(List articleIdList);

    //查看自己的文章数量
    int myHomeArticleCount(int myId, int pageNumber, int cat1);

    //查看他人非草稿等的文章数量
    int otherHomeArticleCount(int userId, int pageNumber);

    //首页自己文章简略加载
    List<Map> myhomeArticle(int myId, int pageNumber, int cat1);

    //查看他人首页文章简略加载
    List<Map> otherHomeArticle(int userId, int pageNumber);

    //将文章删除，delflag改为1
    int articleUpdateDelete(int userId, int articleId, int ref1);

    //修改文章的权限校验
    int articleUpdateAuthority(int userId, int articleId, int ref1);

    //修改文章收藏等
    int articleUpdateInt(int articleId, String type, int operating);

}
