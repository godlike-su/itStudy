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
@Table(name = "navtab")
@NameStyle(Style.normal)
public class Navtab 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	private Integer id ;
	private String name ;
	private Integer sort;
}
 