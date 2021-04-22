package com.itStudy.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.service.AnalysisService;
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
public class analysisUpdateController
{
    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/analysisUpdateDelete.do")
    public Object analysisUpdateDelete(@RequestBody JSONObject jreq) throws Exception
    {
        Long analysisId = null;
        int creator = 0;
        try
        {
            analysisId = jreq.getLong("analysisId");
            creator = jreq.getInteger("creator");
            if(analysisId == null)
            {
                return new AfRestError("错误，未检测到问题，请刷新重试!");
            }
        }
        catch (Exception e)
        {
            return new AfRestError("问题格式不正确!");
        }

        int userId = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        if(creator != userId)
        {
            return new AfRestError("非法删除，非本人发布问题!");
        }
        int i = analysisService.analysisUpdateDelete(analysisId, userId);
        if(i == 0)
        {
            return new AfRestError("删除失败，请刷新重试!");
        }

        return new AfRestData("");
    }


}
