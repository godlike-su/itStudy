package com.itStudy.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.service.AnalysistService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class analysisReplyContoller
{
    @Autowired
    private AnalysistService analysisService;
    @Autowired
    private UserService userService;

    //你问我答回复
    @PostMapping("/user/analysisCommond.do")
    public Object analysisCommond(@RequestBody JSONObject jreq)
    {
        //回复用户的id
        int userid;
        try
        {
            userid = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        } catch (Exception e)
        {
            return new AfRestError("请登录后再评论!");
        }
        //评论的问题id， 和主问题的id
        Long analysisId, ref1;
        int refId;
        String content;  //回复的内容
        try
        {
            analysisId = jreq.getLongValue("analysisId");
            ref1 = jreq.getLongValue("ref1");
            content = jreq.getString("content");
            refId = jreq.getInteger("refId");
            if (content.equals(null) || content.length() <= 0 || content.length() > 300)
            {
                return new AfRestError("字符不符合要求!");
            }
        } catch (Exception e)
        {
            return new AfRestError("不能回复空问题!");
        }
        try
        {
            analysisService.analysisCommond(ref1, analysisId, userid, content, refId);
        } catch (Exception e)
        {
            return new AfRestError(e.getMessage());
        }

        return new AfRestData("");
    }

    //你问我答评论显示
    @PostMapping("/analysisShowReplyList.do")
    public Object analysisShowReplyList(@RequestBody JSONObject jreq)
    {
        Long analysisId;
        int pageNumber;
        try{
            analysisId = jreq.getLongValue("analysisId");
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("信息错误!");
        }

        List<Map> analysisReplyList = null;
        try{
            analysisReplyList = analysisService.analysisShowReplyList(analysisId, pageNumber);
        }catch (Exception e)
        {
            log.error(e.getMessage());
            return new AfRestError(e.getMessage());
        }

        Map data = new HashMap();
        data.put("analysisReplyList", analysisReplyList);
        return new AfRestData(data);
    }

    //你问我答点赞
    @PostMapping("/user/analysisLike.do")
    public Object analysisLike(@RequestBody JSONObject jreq)
    {
        Long analysisId;
        try{
            analysisId = jreq.getLongValue("analysisId");
        }catch (Exception e)
        {
            return new AfRestError("信息错误!");
        }
        int i;
        try{
            i = analysisService.updateAnalysisNumLike(analysisId, true, new Date());
        }catch (Exception e)
        {
            return new AfRestError(e.getMessage());
        }
        return new AfRestData(i);
    }

}
