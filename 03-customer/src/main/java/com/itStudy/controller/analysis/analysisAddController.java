package com.itStudy.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Analysis;
import com.itStudy.entity.User;
import com.itStudy.service.AnalysisService;
import com.itStudy.spring.AfRestData;
import com.itStudy.util.Global;
import com.itStudy.util.MyUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//写入问题
@Controller
@RequestMapping("/analysis")
public class analysisAddController
{
    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/writeAnalysis.do")
    public Object writeAnalysis(@RequestBody JSONObject jreq)
    {
        User user = jreq.getObject("user", User.class);
        Analysis analysis = jreq.getObject("analysis", Analysis.class);
        try{
            Byte draft = jreq.getByte("draft");
            analysis.setDraft(draft);
        }catch (Exception e){}
        analysis.setTimeCreate(new Date());
        analysis.setTimeUpdate(new Date());

        //存储图片路径 示例 201911/01/15725791906031/
        analysis.setStorePath(makeStorePath());
        //图片处理
        analysis.setImg1(moveTmpToStore(analysis.getStorePath(), analysis.getImg1()));
        analysis.setImg2(moveTmpToStore(analysis.getStorePath(), analysis.getImg2()));
        analysis.setImg3(moveTmpToStore(analysis.getStorePath(), analysis.getImg3()));

        analysisService.insertAnalysis(user, analysis);

        return new AfRestData("");
    }

    // 附件图片存储路径 示例 202001/01/15725791906031/
    public static String makeStorePath()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd/");
        return sdf.format(new Date()) + MyUtil.guid2() + "/";
    }
    // 将临时文件存储到Store，并返回存储路径 (此处只存储文件名)
    private String moveTmpToStore(String storePath, String tmpName)
    {
        if (tmpName == null || tmpName.length() == 0) return "";

        File tmpFile = Global.getTmpStore().getFile(tmpName);

        //获取入库图片路径
        File storeDir = Global.getAnalysisStoreStore().getFile(storePath);
//        System.out.println("tmp: " + tmpFile.getAbsolutePath());
//        System.out.println("storeDir: " + storeDir.getAbsolutePath());
        try
        {
            FileUtils.moveFileToDirectory(tmpFile, storeDir, true);
            //图片-1
            return tmpName;
        } catch (Exception e)
        {
//            throw new RuntimeException(e.getMessage());
            return tmpName;
        }
    }

}
