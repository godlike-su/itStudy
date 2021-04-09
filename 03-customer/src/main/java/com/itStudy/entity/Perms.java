package com.itStudy.entity; 

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "perms")
@NameStyle(Style.normal)
public class Perms implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	private Integer id ;
	private String permissions ;
	private String explain ;



} 
 