package com.itStudy.controller.message;

import com.itStudy.service.FollowerService;
import com.itStudy.spring.AfRestData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class followersController
{
    @Autowired
    private FollowerService followerService;

    @PostMapping("/showFollower.do")
    public Object showFollower()
    {
        int id = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
        List<Map> followerList = followerService.showFollowers(id);
        return new AfRestData(followerList);
    }

}
