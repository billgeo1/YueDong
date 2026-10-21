package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Duihuan;


@Mapper
public interface DuihuanMapper
{
	 public int insert(Duihuan duihuan);
	 
	 public int delete(int id);
	 
	 public int update(Duihuan duihuan);
	 
	 public int updateZt(String zt,int id);
	 
	 public List selectAll(String xingming);
	 
	 public List selectByUserId(int userId);
	 
	 public Duihuan findById(int id);
	 
	 
}
