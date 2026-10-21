package com.xiangmu.controler;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.PingjiaMapper;
import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.mappers.UserMapper;
import com.xiangmu.pojo.Pingjia;
import com.xiangmu.util.JsonResult;

@RestController
public class PingjiaControler extends BaseController
{

	@Autowired
	private PingjiaMapper pingjiaMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("/pingjiaAdd")
	public JsonResult<Void> pingjiaAdd(@RequestBody Pingjia pingjia,HttpSession session) 
	{
		pingjia.setUserId(getUidFromSession(session));
		pingjiaMapper.insert(pingjia);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/pingjiaMine")
	public JsonResult<List> pingjiaMine(HttpSession session) 
	{
		List<Pingjia> pingjiaList=pingjiaMapper.selectByUserId(getUidFromSession(session));
		for(Pingjia pingjia : pingjiaList)
		{
			pingjia.setProduct(productMapper.findById(pingjia.getProductId()));
			pingjia.setUser(userMapper.findByUid(pingjia.getUserId()));
		}
		return new JsonResult<>(66,pingjiaList);
		
    }
	
	@RequestMapping("/pingjiaDel")
	public JsonResult<Void> pingjiaDel(int id) 
	{
		pingjiaMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/pingjiaMana")
	public JsonResult<List> pingjiaMana() 
	{
		List<Pingjia> pingjiaList=pingjiaMapper.selectAll("");
		for(Pingjia pingjia : pingjiaList)
		{
			pingjia.setProduct(productMapper.findById(pingjia.getProductId()));
			pingjia.setUser(userMapper.findByUid(pingjia.getUserId()));
		}
		return new JsonResult<>(66,pingjiaList);
		
    }
	
	
	@RequestMapping("/haopingList")
	public JsonResult<List> haopingList(int productId) 
	{
		List<Pingjia> pingjiaList=pingjiaMapper.selectByShuxingAndProduct(productId, "好评");
		for(Pingjia pingjia : pingjiaList)
		{
			pingjia.setUser(userMapper.findByUid(pingjia.getUserId()));
		}
		return new JsonResult<>(66,pingjiaList);
		
    }
	
	
	@RequestMapping("/zhongpingList")
	public JsonResult<List> zhongpingList(int productId) 
	{
		List<Pingjia> pingjiaList=pingjiaMapper.selectByShuxingAndProduct(productId, "中评");
		for(Pingjia pingjia : pingjiaList)
		{
			pingjia.setUser(userMapper.findByUid(pingjia.getUserId()));
		}
		return new JsonResult<>(66,pingjiaList);
		
    }
	
	
	@RequestMapping("/chapingList")
	public JsonResult<List> chapingList(int productId) 
	{
		List<Pingjia> pingjiaList=pingjiaMapper.selectByShuxingAndProduct(productId, "差评");
		for(Pingjia pingjia : pingjiaList)
		{
			pingjia.setUser(userMapper.findByUid(pingjia.getUserId()));
		}
		return new JsonResult<>(66,pingjiaList);
		
    }
	
	
	
	
}
