package com.xiangmu.controler;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangmu.util.JsonResult;
import com.xiangmu.zexception.ServiceException;
import com.xiangmu.zexception.SessionShixiaoException;
import com.xiangmu.zexception.MimaCuowuException;
import com.xiangmu.zexception.ZhanghaoZhanyongException;
import com.xiangmu.zexception.ZhanghaoCuowuException;


public class BaseController
{
    @ExceptionHandler({ServiceException.class})
    @ResponseBody
    public JsonResult<Void> handleException(Throwable e) 
    {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof ZhanghaoZhanyongException) 
        {
        	result.setState(44);
        	//result.setMessage("账号占用");
        }
        else if (e instanceof ZhanghaoCuowuException) 
        {
        	result.setState(45);
        	//result.setMessage("账号错误");
        }
        else if (e instanceof MimaCuowuException) 
        {
        	result.setState(46);
        	//result.setMessage("密码错误");
        }
        else if (e instanceof SessionShixiaoException) 
        {
        	result.setState(100);
        	result.setMessage("session过期，请重新登录");
        }
        else if(1==2)
        {
        	
        }
        return result;
    }
    
    
    protected final int getUidFromSession(HttpSession session)
    {
    	return Integer.valueOf(session.getAttribute("uid").toString());
    }
    
    protected final String getUsernameFromSession(HttpSession session)
    {
    	return session.getAttribute("username").toString();
    }
}
