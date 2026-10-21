package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Shoucang;


@Mapper
public interface ShoucangMapper
{
	 public int insert(Shoucang shoucang);
	 
	 public int delete(int id);
	 
	 public List select(int userId);
	 
	 public List selectByUserAndPro(int userId,int productId);
	 
	 public Shoucang findById(int id);
	 
	 
}
