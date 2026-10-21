package com.xiangmu.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangmu.mappers.CartMapper;
import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.pojo.Cart;

@Service
public class CartService 
{
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	public void cartJiaruProduct(Cart cart,int userId)
	{
		Cart result=cartMapper.selectByUserAndProduct(userId, cart.getProductId());
		
		
		if(result==null)
		{
			cartMapper.insert(cart);
		}
		else
		{
			int zuihoushu=result.getShuliang()+cart.getShuliang();
			int id=result.getId();
			cartMapper.upDate(zuihoushu, id);	
		}
	}
	
	
	public List<Cart> selectCartByUser(int userId)
	{
		List<Cart> cartList=cartMapper.selectCartByUser(userId);
		for(Cart cart :cartList) 
		{
			cart.setProduct(productMapper.findById(cart.getProductId()));
		}
		
		return cartList;
		
	}
	
	
	public int cartDel(int id)
	{
		int rows=cartMapper.del(id);
		return rows;
	}
	
	
	public int delAllByUser(int userId)
	{
		int rows=cartMapper.delAllByUser(userId);
		return rows;
	}
	
	
	public int cartShuliangUpDate(int shuliang,int id)
	{
		int rows=cartMapper.upDate(shuliang, id);
		return rows;
	}
	
}
