package com.itStudyService.shiro.realm;

import com.itStudyService.entity.Perms;
import com.itStudyService.entity.User;
import com.itStudyService.service.UserService;
import com.itStudyService.shiro.salt.MyByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

//自定义realm
public class CustomerRealm extends AuthorizingRealm
{
    @Autowired
    private UserService userService;

    //获取权限信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        //获取身份信息
        String primaryPrincipal = String.valueOf(principals.getPrimaryPrincipal());
        System.out.println("调用校权认证：" + primaryPrincipal);
        //根据主身份信息获取角色 和 权限信息
        User user = userService.findPermsByUserId(Integer.parseInt(primaryPrincipal));

        //授权角色信息
        if(!CollectionUtils.isEmpty(user.getPerms()))
        {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            //获取身份
            simpleAuthorizationInfo.addRole(user.getRole());
            List<Perms> perms = user.getPerms();
            if(!CollectionUtils.isEmpty(perms))
            {
                //添加权限
                perms.forEach(perm->{
                    simpleAuthorizationInfo.addStringPermission(perm.getPermissions());
                });
            }
            return simpleAuthorizationInfo;
        }
        return null;
    }

    //获取身份信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        //根据身份信息
        String principal = (String) token.getPrincipal();

        //在工厂中获取service对象
        User user = userService.findByUserStudentID(principal);
        if(!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(user.getId(),user.getPassword(),
                    new MyByteSource(user.getSalt()),
                    this.getName());
        }
        return null;
    }
}
