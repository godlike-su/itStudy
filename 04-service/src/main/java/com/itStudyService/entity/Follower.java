package com.itStudyService.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/** 本类由 POJO生成器 自动生成于 2021-01-28 15:50:14
    作者：阿发你好      官网: http://afanihao.cn 
*/ 

/** INSERT语句 ( 预处理方式 ) 
  INSERT INTO `follower`
        (`m_id`, `o_id`, `eachother`) 
  VALUES(?, ?, ?) 
*/ 

/** INSERT语句 ( MyBatis方式 ) 
  INSERT INTO `follower`
        (`m_id`, `o_id`, `eachother`) 
  VALUES(#{m_id}, #{o_id}, #{eachother}) 

  自增主键: 无
*/

/**
 * 关注列表
 */
@Table(name = "follower")
@NameStyle(Style.normal)
@Data
public class Follower 
{

	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
	@Id
	private Integer m_id ;
	@Id
	private Integer o_id ;
	private String eachOther ;
	private Date createTime;
}
 