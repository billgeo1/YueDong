package com.xiangmu.pojo;


public class Jifen
{
	private Integer id;
	private Integer userId;
	private Integer fen;
	
	private User user;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getFen() {
		return fen;
	}
	public void setFen(Integer fen) {
		this.fen = fen;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}