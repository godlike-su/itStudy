package com.itStudy.dao;

import com.itStudy.entity.Analysis;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AnalysisDao extends Mapper<Analysis>
{
    List<Map> homeAnalysis(int cat1, int startIndex);

    //根据问题id查找一个问题
    Map findAnalysisById(Long id);

    //修改问题的评论数  true +1
    int updateAnalysisNumReply(Long id, boolean numReply, Date date);

    //修改问题的点赞数  true +1
    int updateAnalysisNumLike(Long id, boolean numLike, Date date);

    //你问我答回复一条评论
    List<Map> analysisShowReplyList(Long analysisId, int number);

    List<Map> myhomeAnalysis(int myId, int startIndex);

    //他人问题首页数据
    List<Map> otherhomeAnalysis(int userId, int startIndex);

    //修改int类型的数据 增加
    int analysisUpdateStartAdd(Long analysisId, String type, int operating);

    //修改int类型的数据 减
    int analysisUpdateStartDec(Long analysisId, String type, int operating);

    //查看搜索的文章
    List<Map> searchAnalysis(String searchContent, int startIndex);


}
