package com.itStudy.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Analysis;
import com.itStudy.entity.Follower;
import com.itStudy.service.AnalysisService;
import com.itStudy.service.StartService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class analysisController
{
    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private UserService userService;
    @Autowired
    private StartService startService;

    //你问我答首页
    @PostMapping("/analysisList.do")
    public Object analysisList(@RequestBody JSONObject jreq)
    {
        int pageNumber = 1;
        int cat1 = 0;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
            cat1 = jreq.getInteger("cat1");
        }catch (Exception e)
        {
            return new AfRestError("");
        }

        int totalitems = analysisService.homeAnalysisCount(cat1);

        //一页显示的数据量
        int pageSize = 10;
        //总页数
        int pageCount = totalitems / pageSize;
        if (totalitems % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> analysisList = analysisService.homeAnalysis(cat1, startIndex);
        JSONObject data = new JSONObject(true);
        data.put("analysisList", analysisList);
        data.put("pageCount", pageCount);
        data.put("totalitems", totalitems);
        return new AfRestData(data);
    }

    //某一条你问我答
    @PostMapping("/analysisOne.do")
    public Object analysisOne(@RequestBody JSONObject jreq)
    {
        Analysis analysis = jreq.getObject("analysis", Analysis.class);
        int startType= jreq.getInteger("startType");

        Map map = analysisService.findAnalysisById(analysis.getId());
        //访问的是是否关注了
        Follower follower = new Follower();
        String m_id = null;
        Map start = new HashMap();
        try{
            m_id = SecurityUtils.getSubject().getPrincipal().toString();
            follower = userService.showFollowerOne(Integer.parseInt(m_id), (Integer) map.get("createId"));
            start = startService.showStartOne(Integer.parseInt(m_id), analysis.getId().intValue(), startType);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if( (Boolean) map.get("draft") || (Integer)map.get("audit")!=0)
        {
            String create = String.valueOf(map.get("createId"));
            if(m_id.equals(create))
            {
                //如果是本人，那么查看不受审核等限制

            }
            else
            {
                return new AfRestError("没找到问题，请试试看其他问题!");
            }

        }

        JSONObject data = new JSONObject(true);
        data.put("analysis", map);
        data.put("follower", follower);
        data.put("start", start.get("start"));
        return new AfRestData(data);
    }



}
