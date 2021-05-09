package com.itStudyService.controller.perms;

import com.alibaba.fastjson.JSONObject;
import com.itStudyService.entity.Perms;
import com.itStudyService.entity.UserPerms;
import com.itStudyService.service.PermsService;
import com.itStudyService.service.UserPermsService;
import com.itStudyService.spring.AfRestData;
import com.itStudyService.spring.AfRestError;
import com.itStudyService.util.ApplicationContextUtils;
import com.itStudyService.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/perms")
public class permsController
{
    @Autowired
    private PermsService permsService;

    @Autowired
    private UserPermsService userPermsService;

    @PostMapping("/showPerms.do")
    public Object showPerms()
    {
        List<Perms> permsList = permsService.showPerms();
        return new AfRestData(permsList);
    }

    @PostMapping("/setUserPerms.do")
    public Object setUserPerms(@RequestBody JSONObject jreq) throws Exception
    {
        int userId = 0;
        String role = jreq.getString("role");
        List authorityList = jreq.getJSONArray("authorityList");
        List<UserPerms> userPermsList = new ArrayList<>();
        try
        {
            userId = jreq.getInteger("userId");
        }catch (Exception e)
        {
            return new AfRestError("检测不到用户，请重试");
        }
        if(role.equals("root"))
        {
            return new AfRestError("无权在此页面更改超级管理员权限！");
        }
        for(int i=0; i<authorityList.size(); i++)
        {
            UserPerms userPerms = new UserPerms();
            userPerms.setUserId(userId);
            userPerms.setPermsId((Integer) authorityList.get(i));
            userPermsList.add(userPerms);
        }

        int i = userPermsService.deletePerms(userId);

        if(authorityList.size()!=0 && !authorityList.isEmpty())
        {
            int j = userPermsService.addUserPerms(userPermsList);
            if(j != 0)
            {
                getRedisTemplate().opsForHash().delete(Global.getAuthorizationCache(),
                        String.valueOf(userId));
            }
        }

        return new AfRestData("");
    }

    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
