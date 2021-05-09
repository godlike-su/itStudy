package com.itStudyService.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user_perms")
@NameStyle(Style.normal)
public class UserPerms
{
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	private Integer id ;

	@Id
	private Integer userId ;
	@Id
	private Integer permsId ;

}
 