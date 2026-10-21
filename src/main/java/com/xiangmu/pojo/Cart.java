package com.xiangmu.pojo;


import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Cart
{
	private int id;
	private int userId;
	private int productId;
	private int shuliang;
	
	private Product product;


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}


	public int getShuliang() {
		return shuliang;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setShuliang(int shuliang) {
		this.shuliang = shuliang;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}

