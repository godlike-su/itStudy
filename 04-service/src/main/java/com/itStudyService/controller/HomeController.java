package com.itStudyService.controller;

import com.itStudyService.entity.Article;
import com.itStudyService.service.Access_recordService;
import com.itStudyService.service.AnalysisService;
import com.itStudyService.service.ArticleService;
import com.itStudyService.service.UserService;
import com.itStudyService.spring.AfRestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController
{
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private Access_recordService access_recordService;


    @PostMapping("/home.do")
    public Object home()
    {
        int userCount = userService.userHomeCount();
        int articleCount = articleService.showArticleAllCount();
        int analysisCount = analysisService.showAnalysisAllCount();
        int accessCount = access_recordService.AccessAllCount();

        Map data = new HashMap();
        data.put("userCount", userCount);
        data.put("articleCount", articleCount);
        data.put("analysisCount", analysisCount);
        data.put("accessCount", accessCount);

        return new AfRestData(data);
    }

    @PostMapping("/WebDataSpace.do")
    public Object WebDataSpace()
    {
        List<Map> timeSort = access_recordService.showTimeSort();
        int accessAdminCount = access_recordService.showTypeAccess("admin");
        int accessUserCount = access_recordService.showTypeAccess("user");
        int accessRootCount = access_recordService.showTypeAccess("root");
        int accessVisitorCount = access_recordService.showTypeAccess("visitor");


        Map data = new HashMap();
        data.put("timeSort", timeSort);
        data.put("accessAdminCount", accessAdminCount);
        data.put("accessUserCount", accessUserCount);
        data.put("accessRootCount", accessRootCount);
        data.put("accessVisitorCount", accessVisitorCount);

        return new AfRestData(data);
    }

}
