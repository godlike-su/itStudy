package com.itStudy.controller.noSecurityUser;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.User;
import com.itStudy.service.AnalysisService;
import com.itStudy.service.ArticleService;
import com.itStudy.service.FansService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class otherHomeController
{
    @Autowired
    private UserService userService;

    @Autowired
    private FansService fansService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AnalysisService analysisService;

    @PostMapping("otherHome.do")
    public Object otherHome(@RequestBody JSONObject jreq) throws Exception
    {
        int id = 0;
        try
        {
            id = jreq.getInteger("id");
        }catch (Exception e)
        {
            return new AfRestError("没有该用户!");
        }

        User user = userService.showInformationByUserid(id);
        if (!ObjectUtils.isEmpty(user))
        {
            //移除不必要的字段
            user.setSalt(null);
            user.setPassword(null);
        }
        else
        {
            return new AfRestError("没有该用户!");
        }

        //粉丝数
        int interestCount = fansService.showInterestCount(id);

        Map data = new HashMap();
        data.put("user", user);
        data.put("interestCount", interestCount);
        return new AfRestData(data);
    }

    //文章
    @PostMapping("/otherHomeArticle.do")
    public Object otherHomeArticle(@RequestBody JSONObject jreq)
    {
        int userId = 0;
        try
        {
            userId = jreq.getInteger("userId");
        }catch (Exception e)
        {
            return new AfRestError("未识别用户，请刷新重试!");
        }
        int pageNumber = 1;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("未识别页码，请刷新重试!");
        }

        //count：查看自己的文章一共有多少条
        int count = articleService.otherHomeArticleCount(userId, pageNumber);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> article = articleService.otherHomeArticle(userId, startIndex);

        JSONObject json = new JSONObject(true);
        json.put("article", article);
        json.put("pageCount", pageCount);

        return new AfRestData(json);
    }

    //他人的问答
    //问答
    @PostMapping("/otherAnalysisList.do")
    public Object otherAnalysisList(@RequestBody JSONObject jreq) throws Exception
    {
        int pageNumber = 1;
        int cat1 = 0;
        int userId = 0;
        try
        {
            userId = jreq.getInteger("userId");
        }catch (Exception e)
        {
            return new AfRestError("未识别用户，请刷新重试!");
        }

        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("没有页码");
        }

        int totalitems = analysisService.otherHomeAnalysisCount(userId);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = totalitems / pageSize;
        if (totalitems % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> analysisList = analysisService.otherhomeAnalysis(userId, startIndex);
        JSONObject data = new JSONObject(true);
        data.put("analysisList", analysisList);
        data.put("pageCount", pageCount);
        data.put("totalitems", totalitems);
        return new AfRestData(data);
    }

}
