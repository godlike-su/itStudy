package com.itStudy.service;

import java.util.List;
import java.util.Map;

public interface FollowerService
{
    //查看关注的数量
    int showFollowersCount(int id);

    //查看我的关注列表
    List<Map> showFollowers(int id, int startIndex, int pageSize);
}
