package com.itStudy.service.Impl;

import com.itStudy.dao.NavtabDao;
import com.itStudy.entity.Navtab;
import com.itStudy.entity.User;
import com.itStudy.service.NavtabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class NavtabServiceImpl implements NavtabService
{

    @Autowired
    private NavtabDao navtabDao;

    @Override
    public  List<Navtab> selectAll()
    {
        Example example = new Example(Navtab.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("sort ASC");
        return navtabDao.selectByExample(example);
    }
}
