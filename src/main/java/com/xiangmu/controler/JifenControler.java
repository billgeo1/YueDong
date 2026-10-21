package com.xiangmu.controler;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.JifenMapper;
import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.mappers.UserMapper;
import com.xiangmu.pojo.Jifen;
import com.xiangmu.pojo.User;
import com.xiangmu.util.JsonResult;

@RestController
public class JifenControler extends BaseController
{

	@Autowired
	private JifenMapper jifenMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	
	
	@RequestMapping("/jifenMine")
	public JsonResult<Jifen> jifenMine(HttpSession session) 
	{
		
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(200);
		}
		
		Jifen jifen=null;
		
		int userId=user.getUid();
		List<Jifen> jifenList=jifenMapper.selectByUserId(userId);
		if(jifenList.size()==0)
		{
			jifen=new Jifen();
			jifen.setId(0);
			jifen.setUserId(userId);
			jifen.setFen(0);
		}
		else
		{
			jifen=jifenList.get(0);
		}
		
		return new JsonResult<>(66,jifen);
		
    }
	
	
}
