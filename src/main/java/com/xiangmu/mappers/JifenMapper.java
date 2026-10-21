package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Gonggao;
import com.xiangmu.pojo.Jifen;


@Mapper
public interface JifenMapper
{
	public int insert(Jifen jifen);
	 
	 public int update(int fen,int userId);
	 
	 public List selectByUserId(int userId);
	 
	 public Jifen findById(int id);
	 
	 
}
