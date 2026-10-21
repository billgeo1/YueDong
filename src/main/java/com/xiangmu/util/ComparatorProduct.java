package com.xiangmu.util;

import java.util.Comparator;

import com.xiangmu.pojo.Product;


public class ComparatorProduct implements Comparator
{

	public int compare(Object o1, Object o2) 
	{
		return ((Product) o2).getXiaoliang() - ((Product) o1).getXiaoliang();
	}


}
