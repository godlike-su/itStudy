package com.itStudyService.config;


import com.itStudyService.shiro.cache.RedisCacheManager;
import com.itStudyService.shiro.filter.ShiroLoginFilter;
import com.itStudyService.shiro.realm.CustomerRealm;
import com.itStudyService.shiro.session.CustomSessionManager;
import com.itStudyService.shiro.session.mySessionDao;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用来整合Shiro框架相关的配置类
 */
@Configuration
public class ShiroConfig
{
//    @Bean(name = "shiroDialect")
//    public ShiroDialect shiroDialect(){
//        return new ShiroDialect();
//    }

    //1、创建shiroFilter  //负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager SecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 1、创建过滤器Map，用来装自定义过滤器
        LinkedHashMap<String, Filter> filterap = new LinkedHashMap<>();

        // 2、将自定义过滤器放入map中，如果实现了自定义授权过滤器，那就必须在这里注册，否则Shiro不会使用自定义的授权过滤器
        filterap.put("authc", new ShiroLoginFilter());

        // 3、将过滤器Ma绑定到shiroFilterFactoryBean上
        shiroFilterFactoryBean.setFilters(filterap);

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(SecurityManager);

        //配置系统受限资源
        Map<String, String> map = new HashMap<String, String>();

//        map.put("/u/**","authc");//authc 请求这个资源需要认证和授权
//        map.put("/user/**", "authc");
//        map.put("/analysis/**", "authc");
        map.put("/login.do","anon");//anon 设置为公共资源
        map.put("/logout.do","anon");//anon 设置为公共资源
        map.put("/photo/**","anon");//anon 设置为公共资源
        map.put("/**", "authc");

        //perms 需要指定权限才能访问
        //roles 需要指定角色访问
        //默认认证界面路径
//        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    //3、创建自定义realm
    @Bean
    public CustomerRealm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        //修改凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);

        //开启缓存管理
//        customerRealm.setCacheManager(cacheManager());
        customerRealm.setCachingEnabled(true);//开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
        customerRealm.setAuthenticationCacheName("AdminAuthenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true);//开启权限授权缓存
        customerRealm.setAuthorizationCacheName("AdminAuthorizationCache");
        return customerRealm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager()
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getRealm());

        //自定义session管理
        securityManager.setSessionManager(sessionManager());
        //自定义缓存实现
        securityManager.setCacheManager(cacheManager());
        //设置管理器记住我
//        securityManager.setRememberMeManager(new CookieManager().rememberMeManager());

        return securityManager;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator()
    {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    //开启aop注解支持
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }

    //自定义缓存
    @Bean
    public CacheManager cacheManager()
    {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        return redisCacheManager;
    }

    @Bean
    //自定义sessionManager实现
    public SessionManager sessionManager()
    {
        CustomSessionManager customSessionManager = new CustomSessionManager();
        //禁用cookie
        customSessionManager.setSessionIdCookieEnabled(false);
        //禁用url重写 url;jsessionid=id
        customSessionManager.setSessionIdUrlRewritingEnabled(false);
        //设置sessionDao的实现，存入redis
        customSessionManager.setSessionDAO(customSessionDaoManager());
        //是否开启删除无效的session对象  默认为true
//        customSessionManager.setDeleteInvalidSessions(true);
        //设置默认缓存时间  10分钟
//        customSessionManager.setGlobalSessionTimeout(600000);
        customSessionManager.setGlobalSessionTimeout(10000);
        return customSessionManager;
    }

    //自定义 SessionDao管理
    @Bean
    public mySessionDao customSessionDaoManager()
    {
        mySessionDao customSessionManager = new mySessionDao();
        return customSessionManager;
    }

    //未登录，进行的操作，返回401状态码, error: 401
//    @Bean
//    public ShiroFilterFactoryBean  shiroLoginFilter()
//    {
//        // 创建 ShiroFilterFactoryBean
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//
//        /* ************* 注册自定义授权过滤器 开始******************/
//
//        // 1、创建过滤器Map，用来装自定义过滤器
//        LinkedHashMap<String, Filter> map = new LinkedHashMap<>();
//
//        // 2、将自定义过滤器放入map中，如果实现了自定义授权过滤器，那就必须在这里注册，否则Shiro不会使用自定义的授权过滤器
//        map.put("authc", new ShiroLoginFilter());
//
//        // 3、将过滤器Ma绑定到shiroFilterFactoryBean上
//        shiroFilterFactoryBean.setFilters(map);
//
//        /* ************* 注册自定义授权过滤器 结束******************/
//        return shiroFilterFactoryBean;
//    }
}
