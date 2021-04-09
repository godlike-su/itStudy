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
@Table(name = "attention")
@NameStyle(Style.normal)
public class Attention
{
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private long id;
    @Id
    private long userId;
    @Id
    private long authorId;
    private Date timeCreate;

}
