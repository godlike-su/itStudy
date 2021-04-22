package com.itStudy.service;

import java.util.List;
import java.util.Map;

public interface StartService
{
    //查看是否有收藏该文章等
    Map showStartOne(int userId, int aId, int startType);

    int insertStart(int userId, int aId, int startType);

    int deletStart(int userId, int aId, int startType);

    int startCount(int userId, int startType);

    List<Map> homeStart(int userId, int startIndex);

    List<Map> myStartAnalysisList(int userId, int startIndex);


}
