package com.itStudy.service;

import com.itStudy.entity.Article;
import com.itStudy.entity.Start;

import java.util.List;
import java.util.Map;

public interface StartService
{
    //查看是否有收藏该文章等
    Map showStartOne(int userId, int aId, int startType);

    int insertStart(int userId, int aId, int startType);

    int deletStart(int userId, int aId, int startType);

    int startCount(int userId);

    List<Article> homeStart(int userId, int startIndex);
}
