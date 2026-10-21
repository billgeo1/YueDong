package com.xiangmu.controler;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.AddressMapper;
import com.xiangmu.pojo.Address;
import com.xiangmu.util.JsonResult;

@RestController
public class AddressControler extends BaseController
{

	@Autowired
	private AddressMapper addressMapper;
	
	
	@RequestMapping("/addressAdd")
	public JsonResult<Void> addressAdd(@RequestBody Address address) 
	{
		addressMapper.insert(address);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/addressMine")
	public JsonResult<List> addressMine(HttpSession session) 
	{
		int userId=getUidFromSession(session);
		List addressList=addressMapper.select("","",userId);
		return new JsonResult<>(66,addressList);
		
    }
	
	@RequestMapping("/addressDel")
	public JsonResult<Void> addressDel(int id) 
	{
		addressMapper.del(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/addressUpdate")
	public JsonResult<Void> update(@RequestBody Address address) 
	{
		addressMapper.update(address);
		return new JsonResult<>(66);
    }
	
	
	
}
