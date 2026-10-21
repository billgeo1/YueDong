package com.xiangmu.controler;



import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.pojo.Cart;
import com.xiangmu.pojo.User;
import com.xiangmu.service.CartService;
import com.xiangmu.util.JsonResult;

@Controller
public class CartControler extends BaseController
{

	@Autowired
	private CartService cartService;
	
	
	@RequestMapping("/addcart")
	@ResponseBody
	public JsonResult<Void> addcart(@RequestBody Cart cart,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(100);
		}
		int userId=getUidFromSession(session);
		cartService.cartJiaruProduct(cart, userId);
        return new JsonResult<>(66);
		
    }
	
	
	@RequestMapping("/cartMinePre")
	@ResponseBody
	public JsonResult<Void> cartMinePre(Cart cart,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(200);
		}
		
        return new JsonResult<>(66);
		
    }
	
	@RequestMapping("/cartMine")
	@ResponseBody
	public JsonResult<List> cartMine(HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		List<Cart> cartList=cartService.selectCartByUser(user.getUid());
		
		int zongjine=0;
		for(Cart cart :cartList) 
		{
			//zongjine=zongjine+cart.getShuliang() * 88888;
		}
		
		return new JsonResult<>(66,cartList);
		
    }
	
	
	
	@RequestMapping("/cartDel")
	@ResponseBody
	public JsonResult<Void> cartDel(int id,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(200);
		}
		
		int rows=cartService.cartDel(id);
        return new JsonResult<>(66);
    }
	
	@RequestMapping("/cartShuliangUpDate")
	@ResponseBody
	public JsonResult<Void> cartShuliangUpDate(int shuliang,int id,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(200);
		}
		
		int rows=cartService.cartShuliangUpDate(shuliang, id);
        return new JsonResult<>(66);
		
    }
	
	@RequestMapping("/cartQingkong")
	@ResponseBody
	public JsonResult<Void> cartQingkong(HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(100);
		}
		
		int rows=cartService.delAllByUser(user.getUid());
        return new JsonResult<>(66);
    }
	
	
	
	@RequestMapping("/jiesuanPre")
	@ResponseBody
	public JsonResult<Void> jiesuanPre(HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(200);
		}
		
		List<Cart> cartList=cartService.selectCartByUser(user.getUid());
		
		if(cartList.size()<=0)
		{
			 return new JsonResult<>(201);
		}
        return new JsonResult<>(66);
		
    }
	
	@RequestMapping("/cartjianshu")
	@ResponseBody
	public JsonResult<String> cartjianshu(HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(1,"?");
		}
		else
		{
			List<Cart> cartList=cartService.selectCartByUser(user.getUid());
			return new JsonResult<>(1,cartList.size()+"件");
		}
		
    }
	
	
	@RequestMapping("/jiesuan")
	public String jiesuan(HttpSession session,Model model) 
	{
		User user=(User)session.getAttribute("user");
		List<Cart> cartList=cartService.selectCartByUser(user.getUid());
		
		int zongjine=0;
		for(Cart cart :cartList) 
		{
			zongjine=zongjine+cart.getShuliang() * 88;
		}
		
		model.addAttribute("zongjine", zongjine);
		return "/view/jiesuan/jiesuan.html";
    }
	
}
