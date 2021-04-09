package com.itStudy.controller.article;

import com.alibaba.fastjson.JSONObject;
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

    @PostMapping("/articleUpdateAuthority.do")
    Object articleUpdateAuthority(@RequestBody JSONObject jreq) throws Exception
    {
        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int articleId = jreq.getInteger("articleId");
        int ref1 = jreq.getInteger("ref1");
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

}
