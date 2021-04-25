package com.itStudy.service;

import com.itStudy.entity.Analysis;
import com.itStudy.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AnalysisService
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

    //查看他人少数据
    int otherHomeAnalysisCount(int userId);

    //自己问题首页数据
    List<Map> myhomeAnalysis(int myId, int startIndex);

    //他人问题首页数据
    List<Map> otherhomeAnalysis(int userId, int startIndex);

    //修改收藏
    int analysisUpdateStart(Long analysisId, String type, int operating);

    //将问题加上删除标签
    int analysisUpdateDelete(Long analysisId,int userId);


    //查看自己的收藏问题
//    List<Map> myStartAnalysisList(int myId, int startIndex);

    //查看搜索问题的数量
    int searchAnalysisCount(String searchContent);

    List<Map> searchAnalysis(String searchContent, int startIndex);


}
