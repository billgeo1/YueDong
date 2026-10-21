package com.xiangmu.controler;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.mappers.ShoucangMapper;
import com.xiangmu.pojo.Gonggao;
import com.xiangmu.pojo.Shoucang;
import com.xiangmu.pojo.User;
import com.xiangmu.util.JsonResult;
import com.xiangmu.zexception.SessionShixiaoException;

@Controller
public class ShoucangControler extends BaseController
{

	@Autowired
	private ShoucangMapper shoucangMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	
	@RequestMapping("/shoucangAdd")
	@ResponseBody
	public JsonResult<Void> shoucangAdd(@RequestBody Shoucang shoucang,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			throw new SessionShixiaoException();
		}
		
		List list=shoucangMapper.selectByUserAndPro(user.getUid(), shoucang.getProductId());//已经收藏
		if(list.size()>0)
		{
			return new JsonResult<Void>(77);
		}
		
		shoucang.setShijian(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		shoucangMapper.insert(shoucang);
		
		return new JsonResult<Void>(66);
		
		
    }
	
	
	@RequestMapping("/shoucangMine")
	@ResponseBody
	public JsonResult<List> shoucangMine(HttpSession session) 
	{
		
		int userId=getUidFromSession(session);
		List<Shoucang> shoucangList=shoucangList=shoucangMapper.select(userId);
		for(Shoucang shoucang:shoucangList)
		{
			shoucang.setProduct(productMapper.findById(shoucang.getProductId()));
		}
        
		return new JsonResult<>(66,shoucangList);
		
    }
	
	
	
	
	
	@RequestMapping("/shoucangDel")
	@ResponseBody
	public JsonResult<Void> shoucangDel(int id) 
	{
		shoucangMapper.delete(id);
		return new JsonResult<Void>(66);
    }
	
}
