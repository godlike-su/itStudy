package com.itStudy.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.User;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class userInformationController
{
    @Autowired
    private UserService userService;

    @Autowired
    private FansService fansService;

    //显示个人信息
    @PostMapping("/showInformation.do")
    public Object showInformation(@RequestBody JSONObject jreq) throws Exception
    {
        int id = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        User user = userService.showInformationByUserid(id);
        if (!ObjectUtils.isEmpty(user))
        {
            //移除不必要的字段
            user.setSalt(null);
            user.setPassword(null);
        }

        //粉丝数
        int interestCount = fansService.showInterestCount(id);

        Map data = new HashMap();
        data.put("user", user);
        data.put("interestCount", interestCount);
        return new AfRestData(data);
    }

    //设置个人信息
    @PostMapping("/setInformation.do")
    public Object setInformation(@RequestBody JSONObject jreq) throws Exception
    {
        Integer id = jreq.getInteger("id");
        String type = jreq.getString("type");
        int i = 0;

        try
        {
            if ("birthday".equals(type))
            {
                Date value = jreq.getDate("value");
                i = userService.setInformation(id, type, value);
            } else if ("sex".equals(type))
            {
                Boolean value = jreq.getBoolean("value");
                i = userService.setInformation(id, type, value);
            } else
            {
                String value = jreq.getString("value");
                i = userService.setInformation(id, type, value);
            }
        }catch (Exception e)
        {
            return new AfRestError("该信息已被注册!");
        }

        return new AfRestData(i);
    }

}
