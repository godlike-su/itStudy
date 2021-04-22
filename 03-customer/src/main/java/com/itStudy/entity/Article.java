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
@Table(name = "article")
@NameStyle(Style.normal)
public class Article
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	private Integer id ; 
	private Integer creator ;
	private String title ; 
	private String text ; 
	private String content ; 
	private Integer cat1 ; 
	private Integer cat2 ; 
	private Integer ref1 ; 
	private Integer ref2 ;
	private Date timeCreate ;
	private Date timeUpdate ; 
	private Byte niceFlag ; 
	private Byte topFlag ; 
	private Byte delFlag ; 
	private String type ; 
	private String address ; 
	private Byte form ; 
	private Byte audit ;
	private Byte draft ; 
	private Integer numReply ; 
	private Integer numStart ;
	private Integer numLike ;
	private String storePath ; 
	private String img1 ; 
	private String img2 ; 
	private String img3 ; 

} 
 