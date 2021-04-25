package com.itStudy.controller.article;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Article;
import com.itStudy.service.ArticleService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/user")
public class articleUpdateController
{
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @PostMapping("/articleUpdateDelete.do")
    Object articleUpdateDelete(@RequestBody JSONObject jreq) throws Exception
    {
        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int articleId = jreq.getInteger("articleId");
        int ref1 = jreq.getInteger("ref1");
        if(userId == ref1)
        {
            int i = articleService.articleUpdateDelete(userId, articleId, ref1);
            if(i!=1)
            {
                return new AfRestError("错误，删除失败!");
            }
        }
        else
        {
            return new AfRestError("错误!");
        }
        return new AfRestData("删除成功");
    }

    //修改文章的权限校验
    @PostMapping("/articleUpdateAuthority.do")
    Object articleUpdateAuthority(@RequestBody JSONObject jreq) throws Exception
    {
        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        Long articleId = jreq.getLong("articleId");
        Long ref1 = jreq.getLongValue("ref1");
        if(userId == ref1)
        {
            int i = articleService.articleUpdateAuthority(userId, articleId, ref1);
            if(i!=1)
            {
                return new AfRestError("错误，无法修改!");
            }
        }
        else
        {
            return new AfRestError("错误!");
        }
        return new AfRestData("200");
    }


    //修改文章
    @PostMapping("/article/updateArticle.do")
    public Object updateArticle(@RequestBody JSONObject jreq)
    {
        Article article = jreq.getObject("article", Article.class);
        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        article.setCreator(userId);
        //再次验证是否本人账号操作
        if(userId != article.getRef1())
        {
            return new AfRestError("修改错误，非本人文章！");
        }
        else
        {
            int i = articleService.articleUpdateAuthority(userId, article.getId(), article.getRef1());
            if(i!=1)
            {
                return new AfRestError("修改错误，非本人文章！");
            }
        }

        article.setTimeCreate(new Date());
        article.setTimeUpdate(new Date());
//        article.setAudit(false);  //这里测试不需要通过审核，到时候删除
//        article.setDraft((byte) 0); //上传就不设置为草稿了
        try
        {
            articleService.updateArticle(article);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new AfRestError(e.getMessage());
        }


        return new AfRestData(article.getId());
    }

}
