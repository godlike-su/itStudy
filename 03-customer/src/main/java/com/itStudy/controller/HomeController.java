package com.itStudy.controller;

import com.itStudy.entity.Navtab;
import com.itStudy.service.NavtabService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController
{
    @Autowired
    private NavtabService navtabService;

    @RequestMapping("/navTab.do")
    public Object navTab() throws Exception
    {
        List<Navtab> navtabList = navtabService.selectAll();

        return new AfRestData(navtabList);
    }

    @GetMapping("/hello")
    public Object hello()
    {
        List<Navtab> navtabList = navtabService.selectAll();
        return new AfRestData(navtabList);
    }

}
