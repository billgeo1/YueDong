package com.xiangmu.controler;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.LiuyanbanMapper;
import com.xiangmu.mappers.UserMapper;
import com.xiangmu.pojo.Liuyanban;
import com.xiangmu.util.JsonResult;

@RestController
public class LiuyanbanControler extends BaseController
{

	@Autowired
	private LiuyanbanMapper liuyanbanMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	
	@RequestMapping("/liuyanbanAdd")
	public JsonResult<Void> liuyanbanAdd(@RequestBody Liuyanban liuyanban) 
	{
		liuyanbanMapper.insert(liuyanban);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/liuyanbanMana")
	public JsonResult<List> liuyanbanMana() 
	{
		List<Liuyanban> liuyanbanList=liuyanbanMapper.select("");
		for(Liuyanban liuyanban :liuyanbanList)
		{
			liuyanban.setUser(userMapper.findByUid(liuyanban.getUser_id()));
		}
		
		return new JsonResult<>(66,liuyanbanList);
		
    }
	
	@RequestMapping("/liuyanbanDel")
	public JsonResult<Void> liuyanbanDel(int id) 
	{
		liuyanbanMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/liuyanbanUpdate")
	public JsonResult<Void> update(@RequestBody Liuyanban liuyanban) 
	{
		liuyanbanMapper.update(liuyanban);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/liuyanbanRes")
	public JsonResult<List> liuyanbanRes(String neirong) 
	{
		List liuyanbanList=liuyanbanMapper.select(neirong.trim());
		return new JsonResult<>(66,liuyanbanList);
		
    }
	
	@RequestMapping("/liuyanbanAll")
	public JsonResult<List> liuyanbanAll(String neirong) 
	{
		List<Liuyanban> liuyanbanList=liuyanbanMapper.select("");
		for(Liuyanban liuyanban :liuyanbanList)
		{
			liuyanban.setUser(userMapper.findByUid(liuyanban.getUser_id()));
		}
		return new JsonResult<>(66,liuyanbanList);
		
    }
	
	
	
}
