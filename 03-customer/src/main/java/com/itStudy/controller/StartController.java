package com.itStudy.controller;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.service.AnalysisService;
import com.itStudy.service.ArticleService;
import com.itStudy.service.StartService;
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

//收藏操作
@Controller
@RequestMapping("/user")
public class StartController
{
    @Autowired
    private StartService startService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AnalysisService analysistService;

    @PostMapping("/updateStart.do")
    Object updateStart(@RequestBody JSONObject jreq) throws Exception
    {
        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        Long aId = jreq.getLong("aId");
        int startType = jreq.getInteger("startType");
        int start = jreq.getInteger("start");
        int i = 0;
        //等于0说明没收藏
        if(start == 0)
        {
            i = startService.insertStart(userId, aId, startType);
            //文章收藏数+1 问题+1
            if(i == 1 && startType == 0)
            {
                articleService.articleUpdateInt(aId, "numStart", 1);
            }
            else if(i == 1 && startType == 1)
            {
                analysistService.analysisUpdateStart(aId, "numStart", 1);
            }
        }
        else if(start == 1)
        {
            //取消收藏
            i = startService.deletStart(userId, aId, startType);
            //文章收藏数-1 问题-1
            if(i == 1 && startType == 0)
            {
                articleService.articleUpdateInt(aId, "numStart", 0);
            }
            else if(i == 1 && startType == 1)
            {
                analysistService.analysisUpdateStart(aId, "numStart", 0);
            }
        }
        else
        {
            //不做处理
            return new AfRestError("错误!");
        }
        return new AfRestData(i);
    }

//    @PostMapping("/showStartAll.do")
    Object showStartAll(@RequestBody JSONObject jreq) throws Exception
    {
        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int pageNumber = 1;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestData("");
        }

        //count：符合条件的记录一共有多少条
        int count = startService.startCount(userId, 0);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> startList = startService.homeStart(userId, startIndex);

        JSONObject json = new JSONObject(true);
        json.put("article", startList);
        json.put("pageCount", pageCount);



        return new AfRestData(json);
    }

}
