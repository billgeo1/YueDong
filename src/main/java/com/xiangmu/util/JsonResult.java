package com.xiangmu.util;

import java.io.Serializable;

//在控制层，设计到一个响应，创建响应，响应分3部分数据，1状态码，2状态描述信息，3响应的数据，所有的控制层都设计到这个操作，
//吧这部分功能封装到这个类，
public class JsonResult<E> implements Serializable
{
	private int state;
	
	//描述信息
	private String message;
		
	//数据信息
	private E data;
	
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public JsonResult()
	{
		
	}
	
	public JsonResult(int state)
	{
		this.state=state;
	}
	
	public JsonResult(Throwable e)
	{
		this.message=e.getMessage();
	}
	
	public JsonResult(int state,E data)
	{
		this.state=state;
		this.data=data;
	}

}
