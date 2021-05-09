package com.itStudyService.controller;

import com.alibaba.fastjson.JSONObject;
import com.itStudyService.client.CustomerClient;
import com.itStudyService.entity.User;
import com.itStudyService.service.UserService;
import com.itStudyService.spring.AfRestClientData;
import com.itStudyService.spring.AfRestData;
import com.itStudyService.spring.AfRestError;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController
{
    @Autowired
    private UserService userService;

    @RequestMapping("/login.do")
    public Object login(@RequestBody JSONObject jreq) throws Exception
    {
        String name = jreq.getString("name");
        String password = jreq.getString("password");
        try
        {
            int studentID = Integer.parseInt(jreq.getString("name"));
        }catch (Exception e)
        {
            return new AfRestError("学号格式错误");
        }

        try
        {
//            UsernamePasswordToken upToken = new UsernamePasswordToken(name, password);
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            //调用login方法
            subject.login(new UsernamePasswordToken(name, password));
            //如果选择保存，则保存7天
            //否则保存30分钟
            subject.getSession().setTimeout(1800000);
            //获取sessionId token
            String sessionId = (String) subject.getSession().getId();

            User user = userService.findByUserStudentID(name);
            if(!ObjectUtils.isEmpty(user))
            {
                if(user.getRole().equals("user"))
                {
                    return new AfRestError("你没有管理员权限！");
                }
            }
            JSONObject object = new JSONObject(true);
            object.put("sessionId", sessionId);
            object.put("name", user.getName());
            object.put("id", user.getId());
            object.put("sex", user.getSex());
            object.put("thumb", user.getThumb());
            object.put("studentID", user.getStudentID());


            //构造返回结果
            return new AfRestData(object);
        }catch (UnknownAccountException e)
        {
            return new AfRestError("用户名错误!");
        } catch (IncorrectCredentialsException e)
        {
            return new AfRestError("密码错误!");
        } catch (Exception e)
        {
            return new AfRestError(e.getMessage());
        }
    }

    @PostMapping("/logout.do")
    public Object logout()
    {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new AfRestData("退出登录");
    }



}
