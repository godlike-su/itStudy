package com.itStudyService.service.Impl;


import com.itStudyService.dao.AnalysisDao;
import com.itStudyService.entity.Analysis;
import com.itStudyService.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysisServiceImpl implements AnalysisService
{
    @Autowired
    private AnalysisDao analysisDao;

    @Override
    public int showAnalysisAllCount()
    {
        Analysis analysis = new Analysis();
        analysis.setRef2(0L);
        return analysisDao.selectCount(analysis);
    }
}
