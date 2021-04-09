package com.itStudy.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Article;
import com.itStudy.service.AnalysistService;
import com.itStudy.service.ArticleService;
import com.itStudy.service.StartService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

//个人主页
@Controller
@RequestMapping("/user")
public class myHomeController
{
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AnalysistService analysisService;

    @Autowired
    private StartService startService;

    //文章
    @PostMapping("/myHomeArticle.do")
    public Object myHomeArticle(@RequestBody JSONObject jreq)
    {
        int myId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int pageNumber = 1;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestData("");
        }

        //count：查看自己的文章一共有多少条
        int count = articleService.myHomeArticleCount(myId, pageNumber, 0);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Article> article = articleService.myhomeArticle(myId, startIndex, 0);

        JSONObject json = new JSONObject(true);
        json.put("article", article);
        json.put("pageCount", pageCount);

        return new AfRestData(json);
    }

    //问答
    @PostMapping("/myAnalysisList.do")
    public Object myAnalysisList(@RequestBody JSONObject jreq)
    {
        int myId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int pageNumber = 1;
        int cat1 = 0;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("没有页码");
        }

        int totalitems = analysisService.myHomeAnalysisCount(myId);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = totalitems / pageSize;
        if (totalitems % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> analysisList = analysisService.myhomeAnalysis(myId, startIndex);
        JSONObject data = new JSONObject(true);
        data.put("analysisList", analysisList);
        data.put("pageCount", pageCount);
        data.put("totalitems", totalitems);
        return new AfRestData(data);
    }

    //收藏的文章
    @PostMapping("/myHomeStart.do")
    public Object myHomeStart(@RequestBody JSONObject jreq)
    {
        int myId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int pageNumber = 1;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestData("");
        }

        //count：查看自己的收藏一共有多少条
        int count = startService.startCount(myId);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Article> article = startService.homeStart(myId, startIndex);

        JSONObject json = new JSONObject(true);
        json.put("article", article);
        json.put("pageCount", pageCount);


        return new AfRestData(json
        );
    }



}
