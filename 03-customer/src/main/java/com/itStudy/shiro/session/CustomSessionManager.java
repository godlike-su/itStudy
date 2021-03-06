package com.itStudy.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义sessionManager
 */
public class CustomSessionManager extends DefaultWebSessionManager
{
    /**
     * 头信息又sessionid
     * 请求头： Authorization: sessionid
     */
    protected Serializable getSessionId(ServletRequest request, ServletResponse response)
    {
        //获取请求头中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if(StringUtils.isEmpty(id))
        {
            //如果没有携带，生成新的sessionId
            return super.getSessionId(request, response);
        }else
        {
            //请求头信息：Bearer sessionid
            id = id.replaceAll("Bearer ", "");
            //返回sessionId;
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }
}
