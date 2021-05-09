package com.itStudyService.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.itStudyService.client.CustomerClient;
import com.itStudyService.entity.User;
import com.itStudyService.service.UserService;
import com.itStudyService.spring.AfRestClientData;
import com.itStudyService.spring.AfRestData;
import com.itStudyService.spring.AfRestError;
import com.itStudyService.util.ApplicationContextUtils;
import com.itStudyService.util.Global;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class userController
{
    @Autowired
    private UserService userService;

    @Autowired
    private CustomerClient customerClient;

    @PostMapping("/userHome.do")
    @RequiresPermissions("admin:update:user")
    public Object userHome(@RequestBody JSONObject jreq) throws Exception
    {
        int pageNumber = 1;
        //一页显示的数据量
        int pageSize = 10;
        try
        {
            pageNumber = jreq.getInteger("pageNumber");
            pageSize = jreq.getInteger("pageSize");
        }catch (Exception e)
        {
            return new AfRestError("检测不到页码，请刷新重试");
        }

        int count = userService.userHomeCount();

        //总页数
        int pageCount = count / pageSize;
        if (count % pageSize != 0) pageCount += 1;
        //查询开始的页数
        int startIndex = pageSize * (pageNumber - 1);

        List<User> userList = userService.userHome(pageSize, startIndex);


        JSONObject json = new JSONObject(true);
        json.put("userList", userList);
        json.put("pageCount", pageCount);
        json.put("total", count);

        return new AfRestData(json);

    }

    @PostMapping("/setUser.do")
    @RequiresPermissions("admin:update:user")
    public Object setUser(@RequestBody JSONObject jreq) throws Exception
    {
        User user = jreq.getObject("user", User.class);
        try
        {
            Integer.parseInt(user.getStudentID());
            if(user.getName().length() < 1)
            {
                return new AfRestError("昵称不能为空");
            }
        }catch (Exception e)
        {
            return new AfRestError("学号格式不正确");
        }
        if(user.getRole().equals("root"))
        {
            return new AfRestError("无权在此页面更改超级管理员信息！");
        }
        user.setTimeUpdate(new Date());
        int i = 0;
        try
        {
            i = userService.setUser(user);
            if(i == 1)
            {
                getRedisTemplate().opsForHash().delete(Global.getAuthenticationCache(),
                        user.getStudentID());
                getRedisTemplate().opsForHash().delete(Global.getAdminauthenticationCache(),
                        user.getStudentID());
            }
        }catch (Exception e)
        {
            return new AfRestError(e.getMessage());
        }
        return new AfRestData(i);
    }

    //管理员更改密码
    @PostMapping("/adminSetPassword.do")
    @RequiresPermissions("admin:update:user")
    public Object adminSetPassword(@RequestBody JSONObject jreq) throws Exception
    {
        int id = 0;
        String password = jreq.getString("password");
        String password2 = jreq.getString("password2");
        String studentID = jreq.getString("studentID");
        try
        {
            id = jreq.getInteger("id");
        }catch (Exception e)
        {
            return new AfRestError("检测不到用户，请重试");
        }

        User user = userService.findByUserId(id);
        if(ObjectUtils.isEmpty(user))
        {
            return new AfRestError("找不到该用户！");
        }
        if(user.getRole().equals("root"))
        {
            return new AfRestError("无权在此页面更改超级管理员密码！");
        }
        if(password.length()<6)
        {
            return new AfRestError("密码需要大于6位，请重新设置!");
        }
        if(!password.equals(password2))
        {
            return new AfRestError("密码与确认密码比对不正确，请重试！");
        }
        password = new Md5Hash(password, user.getSalt(), 1024).toHex();

        int i = userService.updatePassword(id, password);
        //修改成功，退出该用户登录状态，从redis中删除
        if(i == 1)
        {
            getRedisTemplate().opsForHash().delete(Global.getAuthenticationCache(),
                    user.getStudentID());
            getRedisTemplate().opsForHash().delete(Global.getAuthorizationCache(),
                    user.getId());
            getRedisTemplate().opsForHash().delete(Global.getAdminauthenticationCache(),
                    user.getStudentID());
            getRedisTemplate().opsForHash().delete(Global.getAdminauthorizationCache(),
                    user.getId());
            return new AfRestData("200");
        }
        return new AfRestError("密码修改失败，请稍后重试!");

    }

    //用户权限查看
    @PostMapping("/authorityShowOne.do")
    @RequiresPermissions("admin:update:authority")
    public Object authorityShowOne(@RequestBody JSONObject jreq) throws Exception
    {
        int id = 0;
        try
        {
            id = jreq.getInteger("id");
        }catch (Exception e)
        {
            return new AfRestError("检测不到用户，请重试");
        }
        User user = userService.findPermsByUserId(id);
        return new AfRestData(user);
    }

    //查看个人信息
    @PostMapping("/showInformation.do")
    public Object showInformation(@RequestBody JSONObject jreq) throws Exception
    {
        return new AfRestClientData(customerClient.showInformation(jreq));
    }

    //修改个人信息
    @PostMapping("/setInformation.do")
    @RequiresPermissions("admin:update:authority")
    public Object setInformation(@RequestBody JSONObject jreq) throws Exception
    {
        User user = jreq.getObject("user", User.class);
        int id = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        user.setId(id);
        try{
            int studentIDs = Integer.parseInt(user.getStudentID());
            if(user.getStudentID().length() < 1)
            {
                return new AfRestError("学号格式错误");
            }
        }catch (Exception e)
        {
            return new AfRestError("学号格式错误");
        }
        user.setTimeUpdate(new Date());
        try{
            int i = userService.setUser(user);
            if(i ==  1)
            {
                getRedisTemplate().opsForHash().delete(Global.getAuthenticationCache(),
                        user.getStudentID());
                getRedisTemplate().opsForHash().delete(Global.getAdminauthenticationCache(),
                        user.getStudentID());
            }
        }catch (Exception e)
        {
            return new AfRestError(e.getMessage());
        }

        return new AfRestData("200");
    }

    //设置密码
    @PostMapping("/setPassword.do")
    public Object setPassword(@RequestBody JSONObject jreq) throws Exception
    {
        String oldPassword = jreq.getString("oldPassword");
        String password = jreq.getString("password");
        String password2 = jreq.getString("password2");

        if(oldPassword.equals(password))
        {
            return new AfRestError("原密码不能与现密码相同！");
        }

        int id = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        User user = userService.findByUserId(id);
        if(ObjectUtils.isEmpty(user))
        {
            return new AfRestError("找不到该用户！");
        }
        oldPassword = new Md5Hash(oldPassword, user.getSalt(), 1024).toHex();

        if(password.length()<6)
        {
            return new AfRestError("密码需要大于6位，请重新设置!");
        }
        if(!user.getPassword().equals(oldPassword))
        {
            return new AfRestError("原密码错误，请重试！");
        }
        if(!password.equals(password2))
        {
            return new AfRestError("密码与确认密码比对不正确，请重试！");
        }
        password = new Md5Hash(password, user.getSalt(), 1024).toHex();

        int i = userService.updatePassword(id, password);
        //修改成功，退出登录
        if(i == 1)
        {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            getRedisTemplate().opsForHash().delete(Global.getAuthenticationCache(),
                    user.getStudentID());
            getRedisTemplate().opsForHash().delete(Global.getAdminauthenticationCache(),
                    user.getStudentID());
            return new AfRestData("200");
        }

        return new AfRestError("密码修改失败，请稍后重试!");
    }

    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
