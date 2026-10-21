package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Pingjia;


@Mapper
public interface PingjiaMapper
{
	 public int insert(Pingjia pingjia);
	 
	 public int delete(int id);
	 
	 public int update(Pingjia pingjia);
	 
	 public List selectAll(String shuxing);
	 
	 public List selectByUserId(int userId);
	 
	 public List selectByShuxingAndProduct(int productId,String shuxing);
	 
	 public Pingjia findById(int id);
	 
	 
}
