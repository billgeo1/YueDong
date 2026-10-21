package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Order;


@Mapper
public interface OrderMapper
{
	 public int insert(Order order);
	 
	 public int delete(int id);
	 
	 public int updateZt(String zt,int id);
	 
	 public List select(String bianhao,String xiadanshi,String xingming);
	 
	 public Order findById(int id);
	 
	 public List selectByUserId(int userId);
	 
	 
}
