package com.xiangmu.controler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.mappers.AdminMapper;
import com.xiangmu.pojo.Admin;
import com.xiangmu.util.JsonResult;
import com.xiangmu.zexception.MimaCuowuException;
import com.xiangmu.zexception.ZhanghaoCuowuException;



@Controller
public class AdminControler extends BaseController
{
	@Autowired
	private AdminMapper adminMapper;
	
	@RequestMapping("/adminLogin")
	@ResponseBody
	public JsonResult<Admin> adminLogin(String username,String password,HttpSession  session) 
	{
		List adminList=adminMapper.select(username.trim());
		if(adminList.size()<=0)
		{
			throw new ZhanghaoCuowuException();
		}
		
		Admin admin=(Admin)adminList.get(0);
		if(!password.equals(admin.getPassword()))
		{
			throw new MimaCuowuException();
		}
		
		session.setAttribute("admin", admin);
		return new JsonResult<>(66,admin);
    }
	
	
	@RequestMapping("/adminPassUpdate")
	@ResponseBody
	public JsonResult<Void> adminPassUpdate(String oldPass,String newPass,HttpSession  session) 
	{
		Admin admin=(Admin)session.getAttribute("admin");
		
		System.out.println(oldPass+"&&&&&&&&&&&&&&&&&&&&&&&&&"+newPass+"*******"+admin);
		
		if(!(oldPass.trim()).equals(admin.getPassword().trim()))
		{
			throw new MimaCuowuException();
		}
		
		int rows=adminMapper.updateAdminPass(newPass.trim(), admin.getId());
		
		return new JsonResult<>(66);
    }
	
	
	
	@RequestMapping("/adminLogout")
	@ResponseBody
	public JsonResult<Void> adminLogout(HttpSession  session) 
	{
		session.setAttribute("admin", null);
		return new JsonResult<>(66);
    }
	
	
}
