package com.xiangmu.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Leibie
{
	private Integer id;
	private String mingcheng;
	private String jieshao;
	private String del;
	
	private int xiangmushu;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMingcheng() {
		return mingcheng;
	}

	public void setMingcheng(String mingcheng) {
		this.mingcheng = mingcheng;
	}

	public String getJieshao() {
		return jieshao;
	}

	public void setJieshao(String jieshao) {
		this.jieshao = jieshao;
	}

	public String getDel() {
		return del;
	}

	public void setDel(String del) {
		this.del = del;
	}

	public int getXiangmushu() {
		return xiangmushu;
	}

	public void setXiangmushu(int xiangmushu) {
		this.xiangmushu = xiangmushu;
	}

	@Override
	public String toString() {
		return "Zhuanye [id=" + id + ", mingcheng=" + mingcheng + ", jieshao=" + jieshao + ", del=" + del
				+ ", xiangmushu=" + xiangmushu + "]";
	}
	
}