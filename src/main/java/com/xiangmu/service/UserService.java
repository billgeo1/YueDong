package com.xiangmu.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangmu.mappers.UserMapper;
import com.xiangmu.pojo.User;
import com.xiangmu.zexception.MimaCuowuException;
import com.xiangmu.zexception.ZhanghaoZhanyongException;
import com.xiangmu.zexception.ZhanghaoCuowuException;

@Service
public class UserService 
{
	@Autowired
	private UserMapper userMapper;
	
	public void userReg(User user)
	{
		String username=user.getUsername().trim();
		List userList=userMapper.findByUsername(username.trim());
		
		
		if(userList.size() >0)//用户名被占用
		{
			throw new ZhanghaoZhanyongException();//44
		}
		user.setDel("no");
		
		
		int rows=userMapper.insert(user);
	}
	
	public User userLogin(String username,String password)
	{
		List userList=userMapper.findByUsername(username.trim());
		if(userList.size() ==0)
		{
			throw new ZhanghaoCuowuException();//45
		}
		
		User result=(User)userList.get(0);
		
		if(password.equals(result.getPassword())==false)
		{
			throw new MimaCuowuException();
		}
		
		return result;
	}

	
	public void userUpdate(User user)
	{
		
		int rows=userMapper.updateXinxi(user);
		
	}
	
	
	public User findByUid(int uid)
	{
		User user=userMapper.findByUid(uid);
		return user;
		
	}
	
	
	public void userInfoUpdate(User user)
	{
		int rows=userMapper.updateXinxi(user);
		
	}
	
	
	
	
	public List<User> userMana(String username,String xingming)
	{
		List userList= userMapper.select(username, xingming);
		return userList;
		
	}
	
	
	public void userDel(int uid)
	{
		userMapper.del(uid);
	}
}
