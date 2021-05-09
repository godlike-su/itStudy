package com.itStudyService.controller.article;

import com.alibaba.fastjson.JSONObject;
import com.itStudyService.client.CustomerClient;
import com.itStudyService.entity.Navtab;
import com.itStudyService.service.NavtabService;
import com.itStudyService.spring.AfRestClientData;
import com.itStudyService.spring.AfRestData;
import com.itStudyService.spring.AfRestError;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/article")
public class articleNavTabController
{
    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private NavtabService navTabService;

    //查看文章类别
    @PostMapping("/showArticleNavtab.do")
    @RequiresPermissions("admin:select:article")
    public Object showArticleNavtab() throws Exception
    {
        return new AfRestClientData(customerClient.navTab());
    }

    //修改文章类别
    @PostMapping("/updateArticleNavTab.do")
    @RequiresPermissions("admin:update:article")
    public Object updateArticleNavTab(@RequestBody JSONObject jreq) throws Exception
    {
        Navtab navtab = jreq.getObject("navtab", Navtab.class);
        try{
            if(navtab.getName().trim().length() < 1 && navtab.getName().equals(null))
            {
                return new AfRestError("名称长度不能为空");
            }
            if(navtab.getSort() == null)
            {
                return new AfRestError("权重不能为空");
            }
        }catch (Exception e)
        {
            return new AfRestError("修改错误");
        }

        try{
            int i = navTabService.updateNavTab(navtab);
        }catch (Exception e)
        {
            return new AfRestError("字段重复或者其他异常");
        }

        return new AfRestData("");
    }

    @PostMapping("/addArticleNavtab.do")
    @RequiresPermissions("admin:update:article")
    public Object addArticleNavtab(@RequestBody JSONObject jreq) throws Exception
    {
        Navtab navtab = jreq.getObject("navtab", Navtab.class);
        if(navtab.getName().trim().length() < 1 && navtab.getName().equals(null))
        {
            return new AfRestError("名称长度不能为空");
        }

        try{
            int i = navTabService.addNavtab(navtab);
        }catch (Exception e)
        {
            return new AfRestError("字段重复或者其他异常");
        }
        return new AfRestData(navtab);
    }


}
