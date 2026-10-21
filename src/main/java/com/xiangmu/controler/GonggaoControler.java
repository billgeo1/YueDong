package com.xiangmu.controler;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.GonggaoMapper;
import com.xiangmu.pojo.Gonggao;
import com.xiangmu.util.JsonResult;

@RestController
public class GonggaoControler extends BaseController
{

	@Autowired
	private GonggaoMapper gonggaoMapper;
	
	
	@RequestMapping("/gonggaoAdd")
	public JsonResult<Void> gonggaoAdd(@RequestBody Gonggao gonggao) 
	{
		System.out.println(gonggao+"**************");
		gonggaoMapper.insert(gonggao);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/gonggaoMana")
	public JsonResult<List> gonggaoMana() 
	{
		List gonggaoList=gonggaoMapper.select("");
		return new JsonResult<>(66,gonggaoList);
		
    }
	
	@RequestMapping("/gonggaoDel")
	public JsonResult<Void> gonggaoDel(int id) 
	{
		gonggaoMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/gonggaoUpdate")
	public JsonResult<Void> update(@RequestBody Gonggao gonggao) 
	{
		gonggaoMapper.update(gonggao);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/gonggaoRes")
	public JsonResult<List> gonggaoRes(String biaoti) 
	{
		List gonggaoList=gonggaoMapper.select(biaoti.trim());
		System.out.println(gonggaoList.size()+"&&&");
		return new JsonResult<>(66,gonggaoList);
		
    }
	
	@RequestMapping("/gonggaoAll")
	public JsonResult<List> gonggaoAll(String biaoti) 
	{
		List gonggaoList=gonggaoMapper.select("");
		return new JsonResult<>(66,gonggaoList);
		
    }
	
	
	@RequestMapping("/gonggaoDetailQian")
	public JsonResult<Gonggao> gonggaoDetailQian(int id) 
	{
		Gonggao gonggao=gonggaoMapper.findByid(id);
		return new JsonResult<>(66,gonggao);
		
    }
	
	
	@RequestMapping("/gonggaoQian3")
	public JsonResult<List> gonggaoQian4(String biaoti) 
	{
		List gonggaoList=gonggaoMapper.select("");
		if(gonggaoList.size()>3)
		{
			gonggaoList=gonggaoList.subList(0, 3);
		}
		
		return new JsonResult<>(66,gonggaoList);
		
    }
	
	
}
