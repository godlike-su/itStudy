package com.itStudyService.service;


import com.itStudyService.entity.Access_record;

import java.util.List;
import java.util.Map;

public interface Access_recordService
{
    int addAccess(Access_record access_record);

    int AccessAllCount();

    List<Map> showTimeSort();

    int showTypeAccess(String role);
}
