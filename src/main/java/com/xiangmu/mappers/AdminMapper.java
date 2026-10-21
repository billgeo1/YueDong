package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AdminMapper
{
	 public List select(String username); 
	 
	 public int updateAdminPass(String newPass,int id);
}
