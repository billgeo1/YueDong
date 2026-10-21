package com.xiangmu.pojo;


import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User
{
	private int uid;
	private String username;
	private String password;
	private String xingming;
	
	private String xingbie;
	private int nianling;
	private String dizhi;
	private String dianhua;
	
	private String youxiang;
	private String fujian;
	private String shijian;
	private String del;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getXingbie() {
		return xingbie;
	}
	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}
	public int getNianling() {
		return nianling;
	}
	public void setNianling(int nianling) {
		this.nianling = nianling;
	}
	public String getDizhi() {
		return dizhi;
	}
	public void setDizhi(String dizhi) {
		this.dizhi = dizhi;
	}
	public String getDianhua() {
		return dianhua;
	}
	public void setDianhua(String dianhua) {
		this.dianhua = dianhua;
	}
	public String getYouxiang() {
		return youxiang;
	}
	public void setYouxiang(String youxiang) {
		this.youxiang = youxiang;
	}
	
	public String getDel() {
		return del;
	}
	public void setDel(String del) {
		this.del = del;
	}
	
	public String getShijian() {
		return shijian;
	}
	public void setShijian(String shijian) {
		this.shijian = shijian;
	}
	public String getFujian() {
		return fujian;
	}
	public void setFujian(String fujian) {
		this.fujian = fujian;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", xingming=" + xingming
				+ ", xingbie=" + xingbie + ", nianling=" + nianling + ", dizhi=" + dizhi + ", dianhua=" + dianhua
				+ ", youxiang=" + youxiang + ", fujian=" + fujian + ", shijian=" + shijian + ", del=" + del + "]";
	}
	
	
}

