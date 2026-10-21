package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Address;
import com.xiangmu.pojo.Cart;
import com.xiangmu.pojo.User;


@Mapper
public interface CartMapper
{
	 public int insert(Cart cart);
	 
	 public int del(int id);
	 
	 public int delAllByUser(int userId);
	 
	 public int upDate(int shuliang,int id);
	 
	 public Cart selectByUserAndProduct(int userId,int productId);
	 
	 public List selectCartByUser(int userId);
}
