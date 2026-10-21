package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Gonggao;

@Mapper
public interface GonggaoMapper
{
	 public int insert(Gonggao gonggao);
	 
	 public int delete(int id);
	 
	 public int update(Gonggao gonggao);
	 
	 public List select(String biaoti);
	 
	 public Gonggao findByid(int id);
}
