package com.itStudyService.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * 粉丝列表
 */
@Table(name = "start")
@NameStyle(Style.normal)
@Data
public class Start
{

	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	@Id
	private Integer userId ;
	@Id
	private Long aId ;
	private Integer startType ;
	private Date createTime;
}
 