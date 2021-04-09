package com.itStudy.shiro.session;

import com.itStudy.util.ApplicationContextUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class mySessionDao extends AbstractSessionDAO
{
    // Session超时时间，单位为 天
    private long expireTime = 60000;
    //一天
//    private long expireTime = 86400000;

    // 保存到Redis中key的前缀 prefix+sessionId
    private String prefix = "";

//    RedisCache redisCache = new RedisCache();

    public mySessionDao()
    {
        super();
    }

    public mySessionDao(long expireTime)
    {
        super();
        this.expireTime = expireTime;
    }

    public mySessionDao(String prefix)
    {
        super();
        this.prefix =  prefix;
    }

    public mySessionDao(long expireTime, String prefix)
    {
        super();
        this.expireTime = expireTime;
        this.prefix =  prefix;
    }

    //创建session
    @Override
    protected Serializable doCreate(Session session)
    {
//        System.out.println("===============doCreate================");
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
//        System.out.println(session.getTimeout() + ": " + session.getId());
        getRedisTemplate().opsForValue().set(session.getId(), session, session.getTimeout(), TimeUnit.MILLISECONDS);
        return sessionId;
    }

    //读取session
    @Override
    protected Session doReadSession(Serializable sessionId)
    {
//        System.out.println("==============doReadSession=================");
        if (sessionId == null) {
            return null;
        }
        return (Session) getRedisTemplate().opsForValue().get(sessionId);
    }

    //修改session
    @Override
    public void update(Session session) throws UnknownSessionException
    {
//        System.out.println("===============update================");
        if (session == null || session.getId() == null) {
            return;
        }
//        session.setTimeout(expireTime);
//        System.out.println(session.getTimeout());
        getRedisTemplate().opsForValue().set(session.getId(), session, session.getTimeout(), TimeUnit.MILLISECONDS);
//        getRedisTemplate().opsForValue().set(prefix + session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
    }

    //退出删除
    @Override
    public void delete(Session session)
    {
//        System.out.println("===============delete================");
        if (null == session) {
            return;
        }
        getRedisTemplate().opsForValue().getOperations().delete(session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions()
    {
        return null;
    }

    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
