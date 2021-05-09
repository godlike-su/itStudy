package com.itStudyService.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//将string以Result风格返回
public class AfRestClientData implements AfRestView
{
    String data;

    public AfRestClientData(String data)
    {
        this.data = data;
    }


    @Override
    public void render(Map<String, ?> map,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        response.getWriter().print( data );
    }
}
