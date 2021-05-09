package com.itStudyService.controller.article;

import com.alibaba.fastjson.JSONObject;
import com.itStudyService.client.CustomerClient;
import com.itStudyService.entity.Article;
import com.itStudyService.service.ArticleService;
import com.itStudyService.spring.AfRestClientData;
import com.itStudyService.spring.AfRestData;
import com.itStudyService.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class articleController
{
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CustomerClient customerClient;

    //展示所有文章首页
    @PostMapping("/showArticleAll.do")
    public Object showArticleAll(@RequestBody JSONObject jreq) throws Exception
    {
        int pageNumber = 1;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("检测不到页码，请刷新重试");
        }

        //count：符合条件的记录一共有多少条
        int count = articleService.showArticleAllCount();

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> articlList = articleService.showArticleAllCount(startIndex);

        JSONObject json = new JSONObject(true);
        json.put("articleList", articlList);
        json.put("pageCount", pageCount);

        return new AfRestData(json);
    }

    //审核情况
    @PostMapping("/updateArticleAudit.do")
    @RequiresPermissions("admin:update:article")
    public Object updateArticleAudit(@RequestBody JSONObject jreq) throws Exception
    {
        Long id = 0L;
        Byte audit = 1;
        try
        {
            id = jreq.getLongValue("id");
            audit = jreq.getByte("audit");
        }catch (Exception e)
        {
            return new AfRestError("错误，没识别到数据！");
        }
        Article article = new Article();
        article.setId(id);
        article.setAudit(audit);
        article.setTimeUpdate(new Date());

        int i = articleService.updateArticle(article);
        return new AfRestData(i);
    }

    //置顶文章
    @PostMapping("/updateArticleTop.do")
    @RequiresPermissions("admin:update:article")
    public Object updateArticleTop(@RequestBody JSONObject jreq) throws Exception
    {
        long id = 0;
        Byte topFlag = 0;
        try
        {
            id = jreq.getLongValue("id");
            topFlag = jreq.getByte("topFlag");
        }catch (Exception e)
        {
            return new AfRestError("错误，没识别到数据！");
        }
        Article article = new Article();
        article.setId(id);
        article.setTopFlag(topFlag);
        article.setTimeUpdate(new Date());

        int i = articleService.updateArticle(article);
        return new AfRestData(i);
    }

    //删除文章
    @PostMapping("/updateArticleDel.do")
    @RequiresPermissions("admin:update:article")
    public Object updateArticleDel(@RequestBody JSONObject jreq) throws Exception
    {
        long id = 0;
        Byte delFlag = 0;
        try
        {
            id = jreq.getLongValue("id");
            delFlag = jreq.getByte("delFlag");
        }catch (Exception e)
        {
            return new AfRestError("错误，没识别到数据！");
        }
        Article article = new Article();
        article.setId(id);
        article.setDelFlag(delFlag);
        article.setTimeUpdate(new Date());

        int i = articleService.updateArticle(article);
        return new AfRestData(i);
    }





}
