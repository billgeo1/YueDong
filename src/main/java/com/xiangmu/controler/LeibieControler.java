package com.xiangmu.controler;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.LeibieMapper;
import com.xiangmu.pojo.Leibie;
import com.xiangmu.util.JsonResult;

@RestController
public class LeibieControler extends BaseController
{

	@Autowired
	private LeibieMapper leibieMapper;
	
	
	@RequestMapping("/leibieAdd")
	public JsonResult<Void> leibieAdd(@RequestBody Leibie leibie) 
	{
		leibie.setDel("no");
		
		leibieMapper.insert(leibie);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/leibieMana")
	public JsonResult<List> leibieMana() 
	{
		List leibieList=leibieMapper.select("");
		return new JsonResult<>(66,leibieList);
		
    }
	
	@RequestMapping("/leibieDel")
	public JsonResult<Void> leibieDel(int id) 
	{
		leibieMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/leibieUpdate")
	public JsonResult<Void> update(@RequestBody Leibie leibie) 
	{
		leibieMapper.update(leibie);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/leibieRes")
	public JsonResult<List> leibieRes(String mingcheng) 
	{
		List leibieList=leibieMapper.select(mingcheng.trim());
		return new JsonResult<>(66,leibieList);
		
    }
	
	@RequestMapping("/leibieAll")
	public JsonResult<List> leibieAll(String mingcheng) 
	{
		List leibieList=leibieMapper.select("");
		return new JsonResult<>(66,leibieList);
		
    }
	
}
