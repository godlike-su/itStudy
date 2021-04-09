package com.itStudy.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.itStudy.service.UserService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 关注设置
 */

@Controller
@RequestMapping("/user")
public class otherInformationController
{
    @Autowired
    private UserService userService;

    //添加关注或取消关注
    @PostMapping("/setInterest.do")
    public Object setInterest(@RequestBody JSONObject jreq) throws Exception
    {
        int m_id, o_id,follow;
        try{
            //获取自己id
            m_id = Integer.parseInt(SecurityUtils.getSubject().getPrincipal().toString());
            //他人id
            o_id = jreq.getInteger("o_id");
            //关注还是取消
            follow = jreq.getInteger("follow");
            if(m_id == o_id) {
                return new AfRestError("自己不能关注自己！");
            }
        }catch (Exception e)
        {
            return new AfRestError("关注人不存在，请刷新后重试！");
        }
        int i = userService.setInterest(m_id, o_id, follow);
        return new AfRestData(i);
    }


}
