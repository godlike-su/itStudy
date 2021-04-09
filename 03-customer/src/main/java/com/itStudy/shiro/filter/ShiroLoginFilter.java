package com.itStudy.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * shiro 判断是否登录时，不进行重定向，返回json
 * true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
 * 返回 401 表示未登录
 * 返回 403 ， 表示没有权限
 */
public class ShiroLoginFilter extends FormAuthenticationFilter
{
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
//        httpServletResponse.setContentType("text/plain");
        JSONObject json = new JSONObject(true);
        json.put("error", 401);
        json.put("code", 401);
        json.put("status", 401);
        json.put("reason", "登录认证失效，请重新登录");
        httpServletResponse.getWriter().write(JSON.toJSONString(json, SerializerFeature.PrettyFormat));

        return false;
    }
}
