package com.itStudyService.spring;

/**
 * 通用的异常类,带 error和reason两个属性v 
 *
 */
public class AfSpringException extends RuntimeException
{
	public int error = -1;
	public String reason = "";
	
	public AfSpringException()
	{		
	}
	public AfSpringException( int error)
	{	
		this.error = error;
	}
	public AfSpringException( String reason)
	{	
		this.reason = reason;
	}
	public AfSpringException(int error, String reason)
	{
		this.error = error;
		this.reason = reason;
	}
	
	@Override
	public String getMessage()
	{
		return reason + "(" + error + ")";
	}
	
	
}
