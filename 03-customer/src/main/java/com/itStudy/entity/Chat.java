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
@Table(name = "chat")
@NameStyle(Style.normal)
public class Chat
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	private Long id;
	@Id
	private Integer sendUserId ;
	@Id
	private Integer receiveUserId ;
	private String sendMessage ;
	private Date sendTime ;
	private Byte status ;
	private String img1 ;
	private Byte publics ;

}
 