package com.itStudyService.service.Impl;

import com.itStudyService.dao.ArticleDao;
import com.itStudyService.entity.Article;
import com.itStudyService.service.ArticleService;
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
    public int showArticleAllCount()
    {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ref2", 0);

        return articleDao.selectCountByExample(example);
    }

    @Override
    public List<Map> showArticleAllCount(int startIndex)
    {
        return articleDao.showArticleAllCount(startIndex);
    }

    @Override
    public int updateArticle(Article article)
    {
        return articleDao.updateByPrimaryKeySelective(article);
    }

}
