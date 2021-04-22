package com.itStudy.controller.article;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Article;
import com.itStudy.entity.Follower;
import com.itStudy.entity.Start;
import com.itStudy.entity.User;
import com.itStudy.service.ArticleService;
import com.itStudy.service.StartService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class articleController
{

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private StartService startService;

    //首页文章加载
    @PostMapping("/homeArticle.do")
    public Object homeArticle(@RequestBody JSONObject jreq)
    {
        int typeId = 5;
        int pageNumber = 1;
        try
        {
            typeId = jreq.getInteger("typeId");
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestData("");
        }

        //count：符合条件的记录一共有多少条
        int count = articleService.homeArticleCount(typeId);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> article = articleService.homeArticle(typeId, startIndex);

        JSONObject json = new JSONObject(true);
        json.put("article", article);
        json.put("pageCount", pageCount);

        return new AfRestData(json);
    }

    //首页文章热榜加载
    @PostMapping("/homeArticleHold.do")
    public Object homeArticleHold(@RequestBody JSONObject jreq) throws Exception
    {
//        int typeId = 5;
        int pageNumber = 1;
        try
        {
//            typeId = jreq.getInteger("typeId");
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("未识别到页码，请刷新重试!");
        }

        //count：符合条件的记录一共有多少条
        int count = articleService.homeArticleHoldCount();

        //热榜最多加载50条数据
        if(count >= 50)
        {
            count = 50;
        }

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> article = articleService.homeArticleHold(startIndex);

        JSONObject json = new JSONObject(true);
        json.put("article", article);
        json.put("pageCount", pageCount);

        return new AfRestData(json);
    }

    //查看某篇文章
    @PostMapping("/showOneArticle.do")
    public Object showOneArticle(@RequestBody JSONObject jreq)
    {
        int articleId = jreq.getInteger("articleId");
//        int typeId = jreq.getInteger("typeId");
//        String type = jreq.getString("type");
        int startType= jreq.getInteger("startType");
        int typeId = 1;
        Article article = articleService.showOneArticle(articleId, typeId);
        User user = new User();
        if(!ObjectUtils.isEmpty(article))
        {
            //还需查看作者的id，查询作者的信息
            user = userService.findbyArticleRef1(article.getCreator());
        }

        Follower follower = new Follower();
        Map start = new HashMap();
        String m_id = null;
        try{
            m_id = SecurityUtils.getSubject().getPrincipal().toString();
            follower = userService.showFollowerOne(Integer.parseInt(m_id), user.getId());
            start = startService.showStartOne(Integer.parseInt(m_id), articleId, startType);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //如果是草稿或者私密或者未审核只能自己看
        if(article.getAudit()!=0 || article.getDraft()!=0 || article.getForm()!=0)
        {
            String create = String.valueOf(article.getRef1());
            if(m_id.equals(create))
            {

            }
            else
            {
                return new AfRestError("没找到帖子，请试试看其他帖子!");
            }
        }


        //传输数据给前端
        JSONObject data = new JSONObject(true);
        data.put("article", article);
        data.put("user", user);
        data.put("follower", follower);
        data.put("start", start.get("start"));
        return new AfRestData(data);
    }

    //上传文章
    @PostMapping("/article/saveArticle.do")
    public Object saveArticle(@RequestBody JSONObject jreq)
    {
        Article article = jreq.getObject("article", Article.class);
        article.setCreator(Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString()));
        article.setTimeCreate(new Date());
        article.setTimeUpdate(new Date());
//        article.setAudit(false);  //这里测试不需要通过审核，到时候删除
//        article.setDraft((byte) 0); //上传就不设置为草稿了
        try
        {
            articleService.insertSelective(article);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new AfRestError(e.getMessage());
        }


        return new AfRestData(article.getId());
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

    //查看历史记录的文章
    @PostMapping("/historyArticle.do")
    public Object historyArticle(@RequestBody JSONObject jreq)
    {
        List articleIdList = jreq.getJSONArray("articleIdList");
        List<Map> article = articleService.historyArticle(articleIdList);
        return new AfRestData(article);
    }


}
