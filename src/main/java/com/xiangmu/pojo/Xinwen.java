package com.xiangmu.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Xinwen
{
	private Integer id;
	private String biaoti;
	private String neirong;
	private String fujian;
	
	private String fujianYuan;
	private String shijian;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBiaoti() {
		return biaoti;
	}
	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}
	public String getNeirong() {
		return neirong;
	}
	public void setNeirong(String neirong) {
		this.neirong = neirong;
	}
	public String getFujian() {
		return fujian;
	}
	public void setFujian(String fujian) {
		this.fujian = fujian;
	}
	public String getFujianYuan() {
		return fujianYuan;
	}
	public void setFujianYuan(String fujianYuan) {
		this.fujianYuan = fujianYuan;
	}
	public String getShijian() {
		return shijian;
	}
	public void setShijian(String shijian) {
		this.shijian = shijian;
	}
	
}