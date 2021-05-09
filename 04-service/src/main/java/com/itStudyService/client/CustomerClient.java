package com.itStudyService.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "CUSTOMER", url = "119.29.161.208:8081",path = "/itStudyClient")
//@FeignClient(name = "CUSTOMER", path = "/itStudyClient")
public interface CustomerClient
{
    //查看文章类别
    @RequestMapping(value = "/navTab.do", method = RequestMethod.POST)
    String navTab();

    @RequestMapping(value = "/user/showInformation.do", method = RequestMethod.POST)
    String showInformation(@RequestBody JSONObject jreq) throws Exception;

    //设置自己的密码
    @RequestMapping(value = "/user/setPassword.do", method = RequestMethod.POST)
    String setPassword(@RequestBody JSONObject jreq) throws Exception;



}
