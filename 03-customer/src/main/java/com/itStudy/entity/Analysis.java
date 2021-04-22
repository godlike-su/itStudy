package com.itStudy.entity;


import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "analysis")
@NameStyle(Style.normal)
public class Analysis
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Long id;
    private Integer creator;
    private String title;
    private String content;
    private Integer cat1;
    private Integer refId;
    private Long ref1;
    private Long ref2;
    private Date timeCreate;
    private Date timeUpdate;
    private Boolean delFlag;
    private Byte audit;
    private Byte draft;
    private Integer numReply;
    private Integer numStart;
    private Integer numLike;
    private String storePath;
    private String img1;
    private String img2;
    private String img3;
}
