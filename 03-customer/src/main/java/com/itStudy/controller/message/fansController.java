package com.itStudy.controller.message;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.service.FansService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class fansController
{
    @Autowired
    private FansService fansService;

    //查看我的粉丝
    @PostMapping("/showFans.do")
    public Object showFans(@RequestBody JSONObject jreq)
    {
        //自己的id
        int id = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        int pageNumber = 0;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
        }catch (Exception e)
        {
            return new AfRestError("未检测到页码，请刷新重试!");
        }

        int count = fansService.showInterestCount(id);

        //一页显示的数据量
        int pageSize = 18;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> fansList = fansService.showFans(id, startIndex, pageSize);

        Map data = new HashMap();
        data.put("fansList", fansList);
        data.put("pageCount", pageCount);
        return new AfRestData(data);
    }
}
