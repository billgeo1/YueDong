package com.xiangmu.controler;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.mappers.LeibieMapper;
import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.pojo.Product;
import com.xiangmu.util.JsonResult;

@RestController
public class ProductControler extends BaseController
{

	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private LeibieMapper leibieMapper;
	
	@RequestMapping("/productAdd")
	public JsonResult<Void> productAdd(@RequestBody Product product) 
	{
		productMapper.insert(product);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/productMana")
	public JsonResult<List> productMana() 
	{
		List<Product> productList=productMapper.select("");
		for(Product pro : productList)
		{
			pro.setLeibie(leibieMapper.findByid(pro.getLeibieId()));
			
		}
		return new JsonResult<>(66,productList);
		
    }
	
	@RequestMapping("/productDel")
	public JsonResult<Void> productDel(int id) 
	{
		productMapper.delete(id);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/productUpdate")
	public JsonResult<Void> update(@RequestBody Product product) 
	{
		productMapper.update(product);
		return new JsonResult<>(66);
    }
	
	
	@RequestMapping("/productRes")
	public JsonResult<List> productRes(String title) 
	{
		List<Product> productList=productMapper.select(title.trim());
		for(Product pro : productList)
		{
			pro.setLeibie(leibieMapper.findByid(pro.getLeibieId()));
		}
		return new JsonResult<>(66,productList);
		
    }
	
	
	@RequestMapping("/productDetailQian")
	public JsonResult<Product> productDetailQian(int id) 
	{
		Product pro=productMapper.findById(id);
		pro.setLeibie(leibieMapper.findByid(pro.getLeibieId()));
		return new JsonResult<>(66,pro);
		
    }
	
	
	@RequestMapping("/productQian4")
	public JsonResult<List> productQian4() 
	{
		List productList=productMapper.selectAll("");
		if(productList.size()>4)
		{
			productList=productList.subList(0, 4);
		}
		
		return new JsonResult<>(66,productList);
		
    }
	
	
	@RequestMapping("/productByLeibie")
	public JsonResult<List> productByDalei(int leibieId) 
	{
		List productList=productMapper.selectByLeibie(leibieId);
		return new JsonResult<>(66,productList);
		
    }
	
	
	@RequestMapping("/getKucun")
	public JsonResult<Integer> getKucun(int id) 
	{
		Product pro=productMapper.findById(id);
		
		return new JsonResult<>(66,pro.getKucun());
		
    }
	
	
}
