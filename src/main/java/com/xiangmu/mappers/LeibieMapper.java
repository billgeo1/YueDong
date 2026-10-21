package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Leibie;

@Mapper
public interface LeibieMapper
{
	 public int insert(Leibie leibie);
	 
	 public int delete(int id);
	 
	 public int update(Leibie leibie);
	 
	 public List select(String mingcheng);
	 
	 public Leibie findByid(int id);
}
