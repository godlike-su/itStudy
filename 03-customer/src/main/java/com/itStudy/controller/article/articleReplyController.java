package com.itStudy.controller.article;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Article;
import com.itStudy.service.ArticleService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class articleReplyController
{
    @Autowired
    private ArticleService articleService;

    @PostMapping("/user/articleReply.do")
    public Object articleReply(@RequestBody JSONObject jreq) throws Exception
    {
        int userid;
        try
        {
            userid = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        } catch (Exception e)
        {
            return new AfRestError("请登录后再评论!");
        }

        Long articleId = jreq.getLong("articleId");
        int articleCreator = jreq.getInteger("commondId");
        String content = jreq.getString("content");

        //查看原文章
        Article articleOri = articleService.showOneArticle(articleId, 0);
        if(ObjectUtils.isEmpty(articleOri))
        {
            //查不到该文章
            return new AfRestError("错误,找不到该文章，请刷新重试!");
        }


        Article article = new Article();
        article.setCreator(userid);
        article.setTitle("");
        article.setText("");
        article.setContent(content);
        article.setCat1(5);
        article.setRef1(articleOri.getRef1());
        article.setRef2(articleId);
        article.setTimeCreate(new Date());
        article.setTimeUpdate(new Date());
        article.setAudit((byte) 0);  //这里测试不需要通过审核
        article.setDraft((byte) 0); //上传就不设置为草稿了
        article.setForm((byte) 0); //上传就不设置为草稿了
        article.setAddress("");

        int i = 0;
        try
        {
           i =  articleService.insertSelective(article);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new AfRestError("评论失败");
        }

        if(i==1)
        {
            //评论数+1
            articleService.articleReplyAdd(articleId, articleOri.getNumReply());
        }
        else if(i>1)
        {
            articleService.articleReplyAdd(articleId, articleOri.getNumReply());
        }
        else
        {
            return new AfRestError("评论失败");
        }

        return new AfRestData("评论成功");
    }

    @PostMapping("/articleShowReply.do")
    public Object articleShowReply(@RequestBody JSONObject jreq) throws Exception
    {
        Long articleId = null;
        int pageNumber = 0;
        try
        {
            articleId = jreq.getLongValue("articleId");
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("未识别到文章，请刷新重试");
        }

        //count：符合条件的记录一共有多少条
        int count = articleService.ArticleReplyCount(articleId);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> article = articleService.ArticleShowReply(articleId, startIndex);


        Map data = new HashMap();
        data.put("article", article);
        data.put("pageCount", pageCount);
        data.put("pageNumber", pageNumber);
        return new AfRestData(data);
    }

}
