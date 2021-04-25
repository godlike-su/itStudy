package com.itStudy.service;

import java.util.List;
import java.util.Map;

public interface FansService
{
    //查看自己的粉丝数量
    int showInterestCount(int m_id);

    List<Map> showFans(int id, int startIndex, int pageSize);
}
