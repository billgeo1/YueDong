package com.xiangmu.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangmu.pojo.Address;
import com.xiangmu.pojo.Product;
import com.xiangmu.pojo.User;


@Mapper
public interface ProductMapper
{
	 public int insert(Product product);
	 
	 public int delete(int id);
	 
	 public int update(Product product);
	 
	 public int updateKucun(int shuliang,int id);
	 
	 public List select(String title);
	 
	 public List selectAll(String title);
	 
	 public List selectByLeibie(int leibieId);
	 
	 public Product findById(int id);
	 
}
