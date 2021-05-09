package com.itStudy.Task;

import com.itStudy.entity.Analysis;
import com.itStudy.entity.Article;
import com.itStudy.service.AnalysisService;
import com.itStudy.service.ArticleService;
import com.itStudy.util.Global;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class MsgDelTask
{
    @Autowired
    private ArticleService articleService;

    @Autowired
    private AnalysisService analysisService;

    //定时每天凌晨1点删除
    @Scheduled(cron = "0 0 1 * * ?")
    public void run()
    {
        System.out.println("** 清理文章(delFlag=1) ...");
        List<Article> artList = articleService.showDelMessage();
        for(Article art : artList)
        {
            try{
                clearArticle( art);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("** 清理问题(delFlag=1) ...");
        List<Analysis> analysisList = analysisService.showDelAnalysis();
        for(Analysis ana : analysisList)
        {
            try{
                clearAnalysis( ana);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private void clearArticle(Article art) throws Exception
    {
        // 删除附件照片目录
        //获取文件的绝对路径
        File storeDir = Global.getArticleStore().getFile(art.getStorePath());
        if(storeDir.exists())
        {
            try {
                FileUtils.deleteQuietly(storeDir);
            }catch(Exception e)
            {
            }
        }

        // 删除数据库记录
        articleService.deleteArticle(art.getId());
    }

    private void clearAnalysis(Analysis ana) throws Exception
    {
        // 删除附件照片目录
        //获取文件的绝对路径
        File storeDir = Global.getAnalysisStoreStore().getFile(ana.getStorePath());
        if(storeDir.exists())
        {
            try {
                FileUtils.deleteQuietly(storeDir);
            }catch(Exception e)
            {
            }
        }

        // 删除数据库记录
        analysisService.deleteAnalysis(ana.getId());
    }

}
