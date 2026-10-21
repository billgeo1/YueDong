package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Address;
import com.xiangmu.pojo.User;

@Mapper
public interface AddressMapper
{
	 public int insert(Address address);
	 
	 public int del(int id);
	 
	 public int update(Address address);
	 
	 public List select(String xingming,String dizhi,int userId);
	 
	 public Address findById(int id);
	 
}
