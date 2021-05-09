package com.itStudy.controller;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Access_record;
import com.itStudy.entity.User;
import com.itStudy.service.Access_recordService;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import com.itStudy.util.ApplicationContextUtils;
import com.itStudy.util.SaltUtil;
import com.itStudy.util.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController
{
    @Autowired
    private UserService userService;

    @Autowired
    private Access_recordService access_recordService;

    @PostMapping("/login.do")
    public Object login(
            @RequestBody JSONObject jreq) throws Exception
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

        //安全监测，验证码监测
        String verifyToken = jreq.getString("verifyToken").trim();
        String verify = jreq.getString("verify").trim();
        if(verifyToken.length() <= 0)
        {
            return new AfRestError("验证码出错，请刷新页面后重试!");
        }
        if(verify.length() <= 0)
        {
            return new AfRestError("验证码不能为空!");
        }
        else if(verify.length() != 4)
        {
            return new AfRestError("验证码错误!");
        }
        else
        {
            String state = (String) getRedisTemplate().opsForValue().get(verifyToken);
            if(state.equals(null))
            {
                return new AfRestError("验证码已过期，前刷新重试!");
            }
            else if(!state.toUpperCase().equals(verify.toUpperCase()))
            {
                return new AfRestError("验证码错误!");
            }
            else if(state.toUpperCase().equals(verify.toUpperCase()))
            {
                //成功，删除redis的数据
                getRedisTemplate().opsForValue().getOperations().delete(verifyToken);
            }
        }

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
                subject.getSession().setTimeout(1800000);
//                subject.getSession().setTimeout(86400000);
            }
            //获取sessionId token
            String sessionId = (String) subject.getSession().getId();

            User user = userService.findByUserStudentID(name);
            JSONObject object = new JSONObject(true);
            object.put("sessionId", sessionId);
            object.put("name", user.getName());
            object.put("id", user.getId());
            object.put("sex", user.getSex());
            object.put("role", user.getRole());
            object.put("thumb", user.getThumb());
            object.put("studentID", user.getStudentID());

            //访问量+1
            Access_record access_record = new Access_record();
            access_record.setRole(user.getRole());
            access_record.setTimeCreate(new Date());
            access_record.setIsLogin(true);
            access_recordService.addAccess(access_record);

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
        String verifyToken = jreq.getString("verifyToken").trim();
        String verify = jreq.getString("verify").trim();
        if(verifyToken.length() <= 0)
        {
            return new AfRestError("验证码出错，请刷新页面后重试!");
        }
        else if(verify.length() <= 0)
        {
            return new AfRestError("验证码不能为空!");
        }
        else if(verify.length() != 4)
        {
            return new AfRestError("验证码错误!");
        }
        else
        {
            String state = (String) getRedisTemplate().opsForValue().get(verifyToken);
            if(state.equals(null))
            {
                return new AfRestError("验证码已过期，前刷新重试!");
            }
            else if(!state.toUpperCase().equals(verify.toUpperCase()))
            {
                return new AfRestError("验证码错误!");
            }
            else if(state.toUpperCase().equals(verify.toUpperCase()))
            {
                //成功，删除redis的数据
                getRedisTemplate().opsForValue().getOperations().delete(verifyToken);
            }
        }

        User user = new User();
        int studentID = 0;
        try
        {
            studentID = Integer.parseInt(jreq.getString("name"));
        }
        catch (Exception e)
        {
            return new AfRestError("学号格式不正确");
        }

        user.setStudentID(jreq.getString("name"));
        user.setName(jreq.getString("name"));
        user.setPassword(jreq.getString("password"));
        user.setSex(false);
        user.setVipName("非会员");


        user.setBirthday(new Date());
        user.setTimeCreate(new Date());
        user.setTimeLogin(new Date());
        user.setTimeUpdate(new Date());


        if (user.getName() == null)
        {
            return new AfRestError("账号不能为空！");
        }
        else if (user.getName().length() < 1)
        {
            return new AfRestError("昵称不能为空！");
        }
        else if (user.getPassword().length() > 0)
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
            return new AfRestError("学号被占用或者输入格式不正确");
        }

        try
        {
            if(user.getId().equals(null))
            {
                return new AfRestError("学号被占用或者输入格式不正确");
            }
        }
        catch (Exception e)
        {
            return new AfRestError("学号被占用或者输入格式不正确");
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

    //验证码图片
    @RequestMapping("getImage")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String verifyToken = request.getParameter("verifyToken");
        //生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //将验证码放入redis,保存时间为5分钟
        getRedisTemplate().opsForValue().set(verifyToken, code, 300000, TimeUnit.MILLISECONDS);
//        session.setAttribute("code",code);
        //验证码存入图片
        ServletOutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220,60,os,code);
    }

    //添加访问记录
    @PostMapping("/addAcess.do")
    public Object addAcess(@RequestBody JSONObject jreq) throws Exception
    {
        //访问量+1
        Access_record access_record = new Access_record();
        access_record.setRole(jreq.getString("role"));
        access_record.setTimeCreate(new Date());
        if(jreq.getString("role").equals("root")
                || jreq.getString("role").equals("admin")
                || jreq.getString("role").equals("user"))
        {
            access_record.setIsLogin(true);
            access_record.setRole(jreq.getString("role"));
        }
        else
        {
            access_record.setIsLogin(false);
            access_record.setRole("visitor");
        }

        access_recordService.addAccess(access_record);
        return new AfRestData("");
    }

    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
