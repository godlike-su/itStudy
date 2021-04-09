package com.itStudy.entity; 

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
@Table(name = "fans")
@NameStyle(Style.normal)
@Data
public class Fans
{

	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	@Id
	private Integer m_id ;
	@Id
	private Integer o_id ; 
	private String eachOther ;
	private Date createTime;
}
 