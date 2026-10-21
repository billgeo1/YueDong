package com.xiangmu.pojo;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Product
{
	private int id;
	private int leibieId;
	private String title;
	private String jieshao;
	
	private String image;
	private int price;
	private int kucun;
	
	private String del;
	
	private Leibie leibie;
	private int xiaoliang;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLeibieId() {
		return leibieId;
	}
	public void setLeibieId(int leibieId) {
		this.leibieId = leibieId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJieshao() {
		return jieshao;
	}
	public void setJieshao(String jieshao) {
		this.jieshao = jieshao;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getKucun() {
		return kucun;
	}
	public void setKucun(int kucun) {
		this.kucun = kucun;
	}
	public String getDel() {
		return del;
	}
	public void setDel(String del) {
		this.del = del;
	}
	public Leibie getLeibie() {
		return leibie;
	}
	public void setLeibie(Leibie leibie) {
		this.leibie = leibie;
	}
	public int getXiaoliang() {
		return xiaoliang;
	}
	public void setXiaoliang(int xiaoliang) {
		this.xiaoliang = xiaoliang;
	}
	
}

