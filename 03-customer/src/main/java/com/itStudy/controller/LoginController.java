package com.itStudy.controller;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.User;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import com.itStudy.util.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class LoginController
{
    @Autowired
    private UserService userService;

    @PostMapping("/login.do")
    public Object login(
            @RequestBody JSONObject jreq) throws Exception
    {
        String name = jreq.getString("name");
        String password = jreq.getString("password");

        //安全监测，验证码监测
//        String state = jreq.getString("state");
//        if (true)
//        {
//            return new AfRestError("验证码错误或超时，请刷新验证码");
//        }

        boolean isSave = jreq.getBoolean("isSave");
        try
        {
//            UsernamePasswordToken upToken = new UsernamePasswordToken(name, password);
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            //调用login方法
            subject.login(new UsernamePasswordToken(name, password));
            //如果选择保存，则保存7天
            if(isSave)
            {
                subject.getSession().setTimeout(86400000);
            }else {
                //否则保存30分钟
//                subject.getSession().setTimeout(1800000);
                subject.getSession().setTimeout(86400000);
            }
            //获取sessionId token
            String sessionId = (String) subject.getSession().getId();

            User user = userService.findByUserName(name);
            JSONObject object = new JSONObject(true);
            object.put("sessionId", sessionId);
            object.put("name", user.getName());
            object.put("id", user.getId());
            object.put("sex", user.getSex());
            object.put("thumb", user.getThumb());


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

    //注册用户
    @PostMapping("/register.do")
    public Object register(@RequestBody JSONObject jreq) throws Exception
    {
        //安全监测，验证码监测
//        String state = (String) jreq.get("state");
//        if (!state.equalsIgnoreCase((String)session.getAttribute("code")))
//        {
//            return new AfRestError("验证码错误或超时，请刷新验证码");
//        }

//        User user = jreq.getObject("user", User.class);


        User user = new User();

        user.setName(jreq.getString("name"));
        user.setPassword(jreq.getString("password"));
        user.setSex(false);
        user.setVipName("非会员");

        user.setTimeCreate(new Date());
        user.setTimeLogin(new Date());
        user.setTimeUpdate(new Date());

        if (user.getName() == null)
        {
            return new AfRestError("账号不能为空！");
        }
        if (user.getName().length() < 1)
        {
            return new AfRestError("昵称不能为空！");
        }
        if (user.getPassword().length() > 0)
        {
            //明文密码进行md5+salt+hash散列
            String salt = SaltUtil.getSalt(8);
            user.setSalt(salt);
            Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
            user.setPassword(md5Hash.toHex());
        } else
        {
            return new AfRestError("密码不能为空！");
        }

        //注册用户
        try
        {
            userService.register(user);
        } catch (Exception e)
        {
            // id重复时抛出异常
            //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
            return new AfRestError("用户名被占用或者输入格式不正确");
        }

        return new AfRestData(user.getName());
    }

    @PostMapping("/logout.do")
    public Object logout()
    {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new AfRestData("退出登录");
    }
}
