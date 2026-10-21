package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Address;
import com.xiangmu.pojo.Cart;
import com.xiangmu.pojo.OrderItem;
import com.xiangmu.pojo.User;

@Mapper
public interface OrderItemMapper
{
	 public int insert(OrderItem orderItem);
	 
	 public int deleteByOrderId(int orderId);
	 
	 public List selectByOrderId(int orderId);
	 
	 public List selectByProductId(int productId);
	 
}
