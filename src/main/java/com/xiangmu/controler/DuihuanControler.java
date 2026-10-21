package com.xiangmu.controler;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.mappers.DuihuanMapper;
import com.xiangmu.mappers.JifenMapper;
import com.xiangmu.mappers.UserMapper;
import com.xiangmu.pojo.Duihuan;
import com.xiangmu.pojo.Jifen;
import com.xiangmu.pojo.User;
import com.xiangmu.util.JsonResult;

@Controller
public class DuihuanControler extends BaseController
{

	@Autowired
	private JifenMapper jifenMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DuihuanMapper duihuanMapper;
	
	@RequestMapping("/duihuanAdd")
	public String duihuanAdd(Duihuan duihuan,HttpServletRequest request,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		int userId=user.getUid();
		if(user==null)
		{
			request.setAttribute("msg", "session失效，请重新登录");
	        return "/msg/msg.html";
		}
		
		List<Jifen> jifenList=jifenMapper.selectByUserId(getUidFromSession(session));
		Jifen jifen=jifenList.get(0);
		
		if(duihuan.getLipin().equals("聚宝盆"))
		{
			int suoxufen=5000;
			jifenMapper.update(jifen.getFen()-suoxufen, userId);
			
		}
		
		if(duihuan.getLipin().equals("保温杯"))
		{
			int suoxufen=10000;
			jifenMapper.update(jifen.getFen()-suoxufen, userId);
			
		}
		
		duihuan.setUserId(getUidFromSession(session));
		duihuan.setZt("待受理");
		duihuanMapper.insert(duihuan);
		
		request.setAttribute("msg", "操作成功");
        return "/msg/msg.html";
		
    }
	
	
	
	@RequestMapping("/duihuanMine")
	@ResponseBody
	public JsonResult<List> duihuanMine(HttpSession session) 
	{
		List<Duihuan> duihuanList=duihuanMapper.selectByUserId(getUidFromSession(session));
		for(Duihuan duihuan : duihuanList)
		{
			duihuan.setUser(userMapper.findByUid(duihuan.getUserId()));
		}
		return new JsonResult<>(66,duihuanList);
		
    }
	
	@RequestMapping("/duihuanDel")
	@ResponseBody
	public JsonResult<Void> duihuanDel(int id) 
	{
		duihuanMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/duihuanMana")
	@ResponseBody
	public JsonResult<List> duihuanMana() 
	{
		List<Duihuan> duihuanList=duihuanMapper.selectAll("");
		for(Duihuan duihuan : duihuanList)
		{
			duihuan.setUser(userMapper.findByUid(duihuan.getUserId()));
		}
		return new JsonResult<>(66,duihuanList);
		
    }
	
	@RequestMapping("/duihuanShouli")
	@ResponseBody
	public JsonResult<Void> duihuanShouli(String zt,int id) 
	{
		duihuanMapper.updateZt("已受理", id);
		return new JsonResult<>(66);
    }
	
}
