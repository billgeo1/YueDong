package com.xiangmu.controler;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.mappers.AddressMapper;
import com.xiangmu.mappers.CartMapper;
import com.xiangmu.mappers.JifenMapper;
import com.xiangmu.mappers.LeibieMapper;
import com.xiangmu.mappers.OrderItemMapper;
import com.xiangmu.mappers.OrderMapper;
import com.xiangmu.mappers.ProductMapper;
import com.xiangmu.pojo.Address;
import com.xiangmu.pojo.Cart;
import com.xiangmu.pojo.Jifen;
import com.xiangmu.pojo.Order;
import com.xiangmu.pojo.OrderItem;
import com.xiangmu.pojo.Product;
import com.xiangmu.pojo.User;
import com.xiangmu.util.ComparatorProduct;
import com.xiangmu.util.JsonResult;

@Controller
public class OrderControler extends BaseController
{

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private LeibieMapper leibieMapper;
	
	@Autowired
	private JifenMapper jifenMapper;
	
	@RequestMapping("/orderAdd")
	@ResponseBody
	public JsonResult<Void> orderAdd(@RequestBody Order order,HttpSession session) 
	{
		User user=(User)session.getAttribute("user");
		if(user==null)
		{
			return new JsonResult<>(200);
		}
		
		//order.setId(id);
        order.setUserId(user.getUid());
        order.setBianhao(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		order.setXiadanshi(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		order.setZt("待发货");
		
		orderMapper.insert(order);
		
		
		List<Jifen> jifenList=jifenMapper.selectByUserId(user.getUid());
		if(jifenList.size()==0)
		{
			Jifen jifen=new Jifen();
			//jifen.setId(0);
			jifen.setUserId(user.getUid());
			jifen.setFen(order.getZongjine());
			
			jifenMapper.insert(jifen);
		}
		else
		{
			Jifen jifen=jifenList.get(0);
			jifenMapper.update(jifen.getFen()+order.getZongjine(), user.getUid());
		}
		
        List<Cart> cartList=cartMapper.selectCartByUser(getUidFromSession(session));
		for(Cart cart :cartList) 
		{
			cart.setProduct(productMapper.findById(cart.getProductId()));
			
			OrderItem orderItem=new OrderItem();
			
			//orderItem.setId(id);
			orderItem.setOrderId(order.getId());
			orderItem.setProductId(cart.getProductId());
			orderItem.setTitle(productMapper.findById(cart.getProductId()).getTitle());
			
			orderItem.setJiage(cart.getProduct().getPrice());
			orderItem.setShuliang(cart.getShuliang());
			
			orderItemMapper.insert(orderItem);
			
			productMapper.updateKucun(cart.getShuliang(), cart.getProductId());
		}
		
		for(Cart cart :cartList) 
		{
			cartMapper.del(cart.getId());		
		}
		
		
		return new JsonResult<>(66);
		
    }
	
	
	
	@RequestMapping("/orderMine")
	@ResponseBody
	public JsonResult<List> orderMine(HttpSession session) 
	{
		List orderList=orderMapper.selectByUserId(getUidFromSession(session));
		System.out.println(orderList.size()+"&&&");
		return new JsonResult<>(66,orderList);
		
    }
	
	
	@RequestMapping("/orderDel")
	@ResponseBody
	public JsonResult<Void> orderDel(int id) 
	{
		int rows1=orderItemMapper.deleteByOrderId(id);
		
		int rows2=orderMapper.delete(id);
		return new JsonResult<>(66);
		
    }
	
	
	
	@RequestMapping("/mingxi")
	@ResponseBody
	public JsonResult<List> mingxi(int orderId) 
	{
		List<OrderItem> orderItemList=orderItemMapper.selectByOrderId(orderId);
		/*
		 * for(OrderItem orderItem :orderItemList) {
		 * orderItem.setProduct(productMapper.findById(orderItem.getProductId())); }
		 */
		
		return new JsonResult<>(66,orderItemList);
		
    }
	
	
	
	
	@RequestMapping("/orderMana")
	@ResponseBody
	public JsonResult<List> orderMana(HttpSession session) 
	{
		List orderList=orderMapper.select("", "", "");
		return new JsonResult<>(66,orderList);
		
    }
	
	
	
	@RequestMapping("/shouli")
	@ResponseBody
	public JsonResult<Void> shouli(int id) 
	{
		int rows=orderMapper.updateZt("已发货", id);
		return new JsonResult<>(66);
		
    }
	
	
	
	@RequestMapping("/xiaoliangTongji")
	@ResponseBody
	public JsonResult<List> xiaoliangTongji() 
	{
		List<Product> productList=productMapper.select("");
		for(Product pro : productList)
		{
			pro.setLeibie(leibieMapper.findByid(pro.getLeibieId()));
			pro.setXiaoliang(get_xiaoliang_product(pro.getId()));
		}
		
		ComparatorProduct comparator = new ComparatorProduct();
		Collections.sort(productList, comparator);
		
		
		return new JsonResult<>(66,productList);
		
    }
	
	
	
	
	@RequestMapping("/xiaoliangQian4")
	@ResponseBody
	public JsonResult<List> xiaoliangQian4() 
	{
		List<Product> productList=productMapper.select("");
		for(Product pro : productList)
		{
			pro.setXiaoliang(get_xiaoliang_product(pro.getId()));
		}
		
		ComparatorProduct comparator = new ComparatorProduct();
		Collections.sort(productList, comparator);
		
		if(productList.size()>4)
		{
			productList=productList.subList(0, 4);
		}
		
		
		return new JsonResult<>(66,productList);
		
    }
	
	
	
	public int get_xiaoliang_product(int productId) 
	{
		int i=0;
		List<OrderItem> orderItemList=orderItemMapper.selectByProductId(productId);
		
		for(OrderItem orderItem :orderItemList) 
		{
		     i=i+orderItem.getShuliang();
		}
		
		return i;
    }
	
	
	
	
}
