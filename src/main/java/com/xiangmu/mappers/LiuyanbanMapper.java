package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Liuyanban;

@Mapper
public interface LiuyanbanMapper
{
	 public int insert(Liuyanban liuyanban);
	 
	 public int delete(int id);
	 
	 public int update(Liuyanban liuyanban);
	 
	 public List select(String neirong);
	 
	 public Liuyanban findByid(int id);
}
