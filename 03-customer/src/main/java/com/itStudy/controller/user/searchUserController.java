package com.itStudy.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Chat;
import com.itStudy.entity.User;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class searchUserController
{
    @Autowired
    private UserService userService;

    @PostMapping("/searchUser.do")
    public Object searchUser(@RequestBody JSONObject jreq) throws Exception
    {
        int pageNumber = 1;
        String searchContent = null;
        int cat1 = 0;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
            searchContent = jreq.getString("searchContent");
        }catch (Exception e)
        {
            return new AfRestError("没有页码");
        }
        try{
            if(searchContent.equals(null) && searchContent.length()<1)
            {
                return new AfRestError("搜索内容不能为空");
            }
        }catch (Exception e)
        {
            return new AfRestError("搜索内容不能为空");
        }

        int count = userService.searchUserCount(searchContent);

        //一页显示的数据量
        int pageSize = 18;
        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<Map> userList = userService.searchUser(searchContent, startIndex, pageSize);

        Map data = new HashMap();
        data.put("userList", userList);
        data.put("pageCount", pageCount);

        return new AfRestData(data);
    }

}
