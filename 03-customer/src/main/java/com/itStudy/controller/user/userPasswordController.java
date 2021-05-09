package com.itStudy.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.User;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import com.itStudy.util.ApplicationContextUtils;
import com.itStudy.util.Global;
import org.apache.shiro.SecurityUtils;
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

@Controller
@RequestMapping("/user")
public class userPasswordController
{
    @Autowired
    private UserService userService;

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
