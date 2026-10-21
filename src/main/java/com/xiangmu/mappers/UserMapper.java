package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.User;

//@Mapper
//必须的为userMapper.java接口加上@Mapper注解，为该接口实现代理实现类
//如果有很多的mapper接口，都的需要加上，有一种更好的办法，在主启动类上加@MapperScan("com.xiangmu.mappers")

@Mapper
public interface UserMapper
{
	 public int insert(User user);
	 
	 public int del(int uid);
	 
	 public int updateXinxi(User user);
	 
	 public List select(String username,String xingming);
	 
     public List findByUsername(String username);
	 
	 public User findByUid(int uid);
}
