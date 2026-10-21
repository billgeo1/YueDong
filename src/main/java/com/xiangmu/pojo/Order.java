package com.xiangmu.pojo;


import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Order
{
	private int id;
	private int userId;
	private String bianhao;
	private String xiadanshi;
	
	private String dizhi;
	private int zongjine;
	private String fkzt;
	private String fkfs;
	
	private String zt;//待发货-已发货
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBianhao() {
		return bianhao;
	}
	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}
	public String getXiadanshi() {
		return xiadanshi;
	}
	public void setXiadanshi(String xiadanshi) {
		this.xiadanshi = xiadanshi;
	}
	public String getDizhi() {
		return dizhi;
	}
	public void setDizhi(String dizhi) {
		this.dizhi = dizhi;
	}
	public int getZongjine() {
		return zongjine;
	}
	public void setZongjine(int zongjine) {
		this.zongjine = zongjine;
	}
	public String getFkzt() {
		return fkzt;
	}
	public void setFkzt(String fkzt) {
		this.fkzt = fkzt;
	}
	public String getFkfs() {
		return fkfs;
	}
	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
	
}

