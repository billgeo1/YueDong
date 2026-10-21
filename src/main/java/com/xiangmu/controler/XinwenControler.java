package com.xiangmu.controler;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.XinwenMapper;
import com.xiangmu.pojo.Xinwen;
import com.xiangmu.util.JsonResult;

@RestController
public class XinwenControler extends BaseController
{

	@Autowired
	private XinwenMapper xinwenMapper;
	
	
	@RequestMapping("/xinwenAdd")
	public JsonResult<Void> xinwenAdd(@RequestBody Xinwen xinwen) 
	{
		xinwenMapper.insert(xinwen);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/xinwenMana")
	public JsonResult<List> xinwenMana() 
	{
		List xinwenList=xinwenMapper.select("");
		return new JsonResult<>(66,xinwenList);
		
    }
	
	@RequestMapping("/xinwenDel")
	public JsonResult<Void> xinwenDel(int id) 
	{
		xinwenMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/xinwenUpdate")
	public JsonResult<Void> update(@RequestBody Xinwen xinwen) 
	{
		xinwenMapper.update(xinwen);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/xinwenRes")
	public JsonResult<List> xinwenRes(String biaoti) 
	{
		List xinwenList=xinwenMapper.select(biaoti.trim());
		return new JsonResult<>(66,xinwenList);
		
    }
	
	@RequestMapping("/xinwenAll")
	public JsonResult<List> xinwenAll(String biaoti) 
	{
		List xinwenList=xinwenMapper.select("");
		return new JsonResult<>(66,xinwenList);
		
    }
	
	
	@RequestMapping("/xinwenDetailQian")
	public JsonResult<Xinwen> xinwenDetailQian(int id) 
	{
		Xinwen xinwen=xinwenMapper.findByid(id);
		return new JsonResult<>(66,xinwen);
		
    }
	
	
	@RequestMapping("/xinwenQian3")
	public JsonResult<List> xinwenQian3(String biaoti) 
	{
		List xinwenList=xinwenMapper.select("");
		if(xinwenList.size()>3)
		{
			xinwenList=xinwenList.subList(0, 3);
		}
		
		return new JsonResult<>(66,xinwenList);
		
    }
	
	
}
