package com.xiangmu.controler;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.pojo.User;
import com.xiangmu.service.UserService;
import com.xiangmu.util.JsonResult;

@Controller
public class UserControler extends BaseController
{

	@Autowired
	private UserService userService;
	
	int pagesize=8;
	
	@RequestMapping("/userReg")
	@ResponseBody
	public JsonResult<Void> userReg(@RequestBody User user) //@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交
	{
        userService.userReg(user);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/userLogin")
	@ResponseBody
	public JsonResult<User> userLogin(String username,String password,HttpSession session) 
	{
        User user= userService.userLogin(username, password);
        
        session.setAttribute("uid", user.getUid());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("user", user);
        
        System.out.println(getUidFromSession(session));
        System.out.println(getUsernameFromSession(session));
        
        return new JsonResult<User>(66,user);
		
    }
	
	
	@RequestMapping("/userUpdate")
	@ResponseBody
	public JsonResult<Void> userUpdate(@RequestBody User user) 
	{
        
        userService.userUpdate(user);
        return new JsonResult<Void>(66);
		
    }
	
	
	@RequestMapping("/userMana")
	@ResponseBody
	public JsonResult<List> userMana() 
	{
		List userList=userService.userMana("","");
		return new JsonResult<>(66,userList);
		
    }
	
	@RequestMapping("/userDel")
	@ResponseBody
	public JsonResult<Void> userDel(int uid) 
	{
		userService.userDel(uid);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/userRes")
	@ResponseBody
	public JsonResult<List> userRes(String username,String xingming) 
	{
		List userList=userService.userMana(username.trim(),xingming.trim());
		return new JsonResult<>(66,userList);
		
    }
	
	
}
