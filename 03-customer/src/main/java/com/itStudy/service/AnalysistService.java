package com.itStudy.service;

import com.itStudy.entity.Analysis;
import com.itStudy.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AnalysistService
{
    //插入一条问题
    int insertAnalysis(User user, Analysis analysis);

    //查看首页有多少条符合的数据
    int homeAnalysisCount(int cat1);

    //你问我答首页数据
    List<Map> homeAnalysis(int cat1, int startIndex);

    Map findAnalysisById(Long id);

    //评论一条问题
    int analysisCommond(Long ref1, Long analysisId, int userId, String content, int refId);

    //显示评论
    List<Map> analysisShowReplyList(Long analysisId, int number);

    //点赞
    int updateAnalysisNumLike(Long id, boolean numLike, Date date);

    //查看自己有多少数据
    int myHomeAnalysisCount(int myId);

    //自己首页数据
    List<Map> myhomeAnalysis(int myId, int startIndex);


}
