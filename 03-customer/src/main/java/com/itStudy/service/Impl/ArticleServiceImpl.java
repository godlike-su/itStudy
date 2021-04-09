package com.itStudy.service.Impl;

import com.itStudy.dao.ArticleDao;
import com.itStudy.entity.Article;
import com.itStudy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService
{

    @Autowired
    private ArticleDao articleDao;

    @Override
    public int insertSelective(Article article)
    {
        return articleDao.insertSelective(article);
    }

    @Override
    public int updateArticle(Article article)
    {
        return articleDao.updateByPrimaryKeySelective(article);
    }

    @Override
    public List<Article> homeArticle(int cat1, Integer pageNumber)
    {
        return articleDao.homeArticle(cat1, pageNumber);
    }

    @Override
    public int homeArticleCount(int cat1)
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cat1", cat1);
        criteria.andEqualTo("draft", 0);  //不为草稿
        criteria.andEqualTo("audit", 0);    //通过审核
        criteria.andEqualTo("form", 0);     //发布为公平模式
        criteria.andEqualTo("delFlag", 0);     //不是删除文章

        return articleDao.selectCountByExample(example);
    }

    @Override
    public Article showOneArticle(int id, int cat1)
    {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
//        criteria.andEqualTo("cat1", cat1);
//        criteria.andEqualTo("draft", 0);  //不为草稿
//        criteria.andEqualTo("audit", 0);    //通过审核
//        criteria.andEqualTo("form", 0);     //发布为公开模式
        criteria.andEqualTo("delFlag", 0);     //不是删除文章

        return articleDao.selectOneByExample(example);
    }

    @Override
    public List<Article> historyArticle(List articleIdList)
    {
        return articleDao.historyArticle(articleIdList);
    }

    @Override
    public int myHomeArticleCount(int myId, int pageNumber, int cat1)
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cat2", 0);   //原帖
        criteria.andEqualTo("ref1", myId);     //不是删除文章
        criteria.andEqualTo("delFlag", 0);     //不是删除文章

        return articleDao.selectCountByExample(example);
    }

    @Override
    public List<Article> myhomeArticle(int myId, int pageNumber, int cat1)
    {
        return articleDao.myhomeArticle(myId, pageNumber, 0);
    }

    @Override
    public int articleUpdateDelete(int userId, int articleId, int ref1)
    {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref1", userId);
        criteria.andEqualTo("id", articleId);
        return articleDao.deleteByExample(example);
    }

    @Override
    public int articleUpdateAuthority(int userId, int articleId, int ref1)
    {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref1", userId);
        criteria.andEqualTo("id", articleId);
        return articleDao.selectCountByExample(example);
    }

    @Override
    public int articleUpdateInt(int articleId, String type, int operating)
    {
        int i = 0;
        //等于0说明要执行减操作
        if(operating == 0)
        {
            i = articleDao.articleUpdateStartDec(articleId, type, 0);
        }
        //等于0说明要执行增操作
        else if(operating == 1)
        {
            i = articleDao.articleUpdateStartAdd(articleId, type, 1);
        }
        else
        {
            System.out.println("没有任何操作!");
            return 0;
        }
        return 0;
    }


}
