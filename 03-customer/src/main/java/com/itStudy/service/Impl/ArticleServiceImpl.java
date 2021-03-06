package com.itStudy.service.Impl;

import com.itStudy.dao.ArticleDao;
import com.itStudy.entity.Article;
import com.itStudy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

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
    public List<Map> homeArticle(int cat1, Integer pageNumber)
    {
        return articleDao.homeArticle(cat1, pageNumber);
    }

    @Override
    public List<Map> homeArticleHold(Integer pageNumber)
    {

        return articleDao.homeArticleHold(pageNumber);
    }

    @Override
    public int homeArticleCount(int cat1)
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cat1", cat1);
        criteria.andEqualTo("ref2", 0);  //不是评论
        criteria.andEqualTo("draft", 0);  //不为草稿
        criteria.andEqualTo("audit", 0);    //通过审核
        criteria.andEqualTo("form", 0);     //发布为公开模式
        criteria.andEqualTo("delFlag", 0);     //不是删除文章

        return articleDao.selectCountByExample(example);
    }

    @Override
    public int homeArticleHoldCount()
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref2", 0);    //不是评论
        criteria.andEqualTo("draft", 0);  //不为草稿
        criteria.andEqualTo("audit", 0);    //通过审核
        criteria.andEqualTo("form", 0);     //发布为公开模式
        criteria.andEqualTo("delFlag", 0);     //不是删除文章

        return articleDao.selectCountByExample(example);
    }

    @Override
    public Article showOneArticle(Long id, int cat1)
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
    public List<Map> historyArticle(List articleIdList)
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
    public int otherHomeArticleCount(int userId, int pageNumber)
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cat2", 0);   //原帖
        criteria.andEqualTo("creator", userId);     //创作者id
        criteria.andEqualTo("delFlag", 0);     //不是删除文章
        criteria.andEqualTo("audit", 0);     //审核通过文章
        criteria.andEqualTo("form", 0);     //公开文章
        criteria.andEqualTo("draft", 0);     //不是草稿

        return articleDao.selectCountByExample(example);
    }

    @Override
    public List<Map> myhomeArticle(int myId, int pageNumber, int cat1)
    {
        return articleDao.myhomeArticle(myId, pageNumber, 0);
    }

    @Override
    public List<Map> otherHomeArticle(int userId, int pageNumber)
    {
        return articleDao.otherHomeArticle(userId, pageNumber);
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
    public int articleUpdateAuthority(int userId, Long articleId, Long ref1)
    {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref1", userId);
        criteria.andEqualTo("id", articleId);
        return articleDao.selectCountByExample(example);
    }

    @Override
    public int articleUpdateInt(Long articleId, String type, int operating)
    {
        int i = 0;
        //等于0说明要执行减操作
        if(operating == 0)
        {
            i = articleDao.articleUpdateStartDec(articleId, type, 0);
        }
        //等于1说明要执行增操作
        else if(operating == 1)
        {
            i = articleDao.articleUpdateStartAdd(articleId, type, 1);
        }
        else
        {
            System.out.println("没有任何操作!");
            return 0;
        }
        return i;
    }

    @Override
    public int articleReplyAdd(Long articleId, int numReply)
    {
        Article article = new Article();
        article.setId(articleId);
        article.setNumReply(numReply + 1);
        return articleDao.updateByPrimaryKeySelective(article);
    }

    @Override
    public int ArticleReplyCount(Long articleId)
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref2", articleId);    //不是评论
        criteria.andEqualTo("audit", 0);    //通过审核
        criteria.andEqualTo("delFlag", 0);     //不是删除文章

        return articleDao.selectCountByExample(example);
    }

    @Override
    public List<Map> ArticleShowReply(Long articleId, int startIndex)
    {
        return articleDao.ArticleShowReply(articleId, startIndex);
    }

    @Override
    public int searchArticleCount(String searchContent)
    {
        //查询总数的筛选条件
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref2", 0);    //不是评论
        criteria.andEqualTo("draft", 0);  //不为草稿
        criteria.andEqualTo("audit", 0);    //通过审核
        criteria.andEqualTo("form", 0);     //发布为公开模式
        criteria.andEqualTo("delFlag", 0);     //不是删除文章
        criteria.andLike("title", "%"+searchContent+"%");
        criteria.orLike("text", "%"+searchContent+"%");

        return articleDao.selectCountByExample(example);
    }

    @Override
    public List<Map> searchArticle(String searchContent, int startIndex)
    {
        return articleDao.searchArticle("%"+searchContent+"%", startIndex);
    }


}
