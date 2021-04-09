package com.itStudy.service.Impl;

import com.itStudy.dao.AnalysisDao;
import com.itStudy.entity.Analysis;
import com.itStudy.entity.User;
import com.itStudy.service.AnalysistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AnalysistServiceImpl implements AnalysistService
{
    @Autowired
    private AnalysisDao analysisDao;

    @Override
    public int insertAnalysis(User user, Analysis analysis)
    {
        return analysisDao.insertSelective(analysis);
    }

    @Override
    public int homeAnalysisCount(int cat1)
    {
        Example example = new Example(Analysis.class);
        Example.Criteria criteria = example.createCriteria();
        if(cat1 != 0) {
            criteria.andEqualTo("cat1", cat1);
        }
        criteria.andEqualTo("draft", 0);  //不为草稿
        criteria.andEqualTo("audit", 0);    //通过审核
        criteria.andEqualTo("delFlag", 0);     //不是删除文章
        criteria.andEqualTo("ref1", 0);     //为主贴
        return analysisDao.selectCountByExample(example);
    }

    @Override
    public List<Map> homeAnalysis(int cat1, int startIndex)
    {
        return analysisDao.homeAnalysis(cat1, startIndex);
    }

    @Override
    public Map findAnalysisById(Long id)
    {
        return analysisDao.findAnalysisById(id);
    }

    @Override
    public int analysisCommond(Long ref1, Long analysisId, int userId, String content, int refId)
    {
        Analysis analysis = new Analysis();
        analysis.setCreator(userId);
        analysis.setContent(content);
        analysis.setTitle("");
        analysis.setCat1(0);
        analysis.setRefId(refId);
        analysis.setRef1(ref1);
        if(!ref1.equals(analysisId))
        {
            analysis.setRef2(analysisId);
        }
        analysis.setTimeCreate(new Date());
        analysis.setTimeUpdate(new Date());
        analysis.setStorePath("");
        analysis.setImg1("");
        analysis.setImg2("");
        analysis.setImg3("");

        int i = 0;
        try{
            //插入一条回复
            i = analysisDao.insertSelective(analysis);
            //原问题需要修改
            analysisDao.updateAnalysisNumReply(ref1, true, new Date());
        }catch (Exception e)
        {
            log.error(e.getMessage());
        }

        return i;
    }

    @Override
    public List<Map> analysisShowReplyList(Long analysisId, int number)
    {
        return analysisDao.analysisShowReplyList(analysisId, number);
    }

    @Override
    public int updateAnalysisNumLike(Long id, boolean numLike, Date date)
    {
        return analysisDao.updateAnalysisNumLike(id, numLike, date);
    }

    @Override
    public int myHomeAnalysisCount(int myId)
    {
        Example example = new Example(Analysis.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("creator", myId);  //不为草稿
        criteria.andEqualTo("delFlag", 0);     //不是删除文章
        criteria.andEqualTo("ref1", 0);     //为主贴
        return analysisDao.selectCountByExample(example);
    }

    @Override
    public List<Map> myhomeAnalysis(int myId, int startIndex)
    {
        return analysisDao.myhomeAnalysis(myId, startIndex);
    }
}
