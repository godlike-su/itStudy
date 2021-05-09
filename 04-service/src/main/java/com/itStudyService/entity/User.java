package com.itStudyService.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "user")
@NameStyle(Style.normal)
public class User implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	private Integer id ;
	private String name ;
	private String password ;
	private Boolean sex ;
	private Date birthday;
	private String qqid ;
	private Boolean qqFlag ;
	private String qqName ;
	private String email ;
	private Boolean emailFlag ;
	private String phone ;
	private Boolean phoneFlag ;
	private String thumb ;
	private Byte level ;
	private Integer experience ;
	private Byte vip ;
	private String vipName ;
	private Date timeCreate ;
	private Date timeUpdate ;
	private Date timeLogin ;
	private String salt ;
	private String studentID ;
	private String school ;
	private String profession ;
	private String introduction ;
	private String role;
	private Byte hide;

	//权限集合
	private List<Perms> perms;

} 
 