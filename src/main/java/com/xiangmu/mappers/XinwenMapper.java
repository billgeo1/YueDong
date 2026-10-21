package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Xinwen;

@Mapper
public interface XinwenMapper
{
	 public int insert(Xinwen xinwen);
	 
	 public int delete(int id);
	 
	 public int update(Xinwen xinwen);
	 
	 public List select(String biaoti);
	 
	 public Xinwen findByid(int id);
}
