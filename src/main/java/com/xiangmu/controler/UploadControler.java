package com.xiangmu.controler;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xiangmu.util.JsonResult;
import com.xiangmu.util.Fujian;

@Controller
public class UploadControler extends BaseController
{
	
	public static final int Max_size=10 *1024 *1024;//最大值10M
	
	public static final List<String> fileTypeList =new ArrayList<String>();
	static 
	{
		
	}
	
	@RequestMapping("/upload")
	@ResponseBody
	public JsonResult<Fujian> upload(HttpSession session, MultipartFile file) 
	{
		System.out.println("文件大小****************************："+file.getSize());
		String parent=session.getServletContext().getRealPath("upload");
		
		File dir=new File(parent);
		if( ! dir.exists())
		{
			dir.mkdirs();
		}
		
		String oldFileName=file.getOriginalFilename();
		System.out.println(oldFileName+"&&&&&&&&&&&&&&&&&&&&");
		
		int index=oldFileName.lastIndexOf(".");
		String suffix=oldFileName.substring(index);
		
		String newFileName=UUID.randomUUID().toString().toUpperCase()+suffix;//
		System.out.println(newFileName+"#########################################:新文件的名字是");
		
		File dest=new File(dir,newFileName);
		
		try
		{
			file.transferTo(dest);
		}
		catch (Exception e) 
		{
			System.out.println("文件读写异常");
		}
		
		String fujian="/upload/"+newFileName;
		return new JsonResult<>(66,new Fujian(fujian,oldFileName));
    }
	
	
	
	@RequestMapping("/down")
	public String down(HttpServletRequest request, HttpServletResponse response,String url) throws IOException
	{
	    String filePath = request.getSession().getServletContext().getRealPath("/");
	    
	    String fileName = url;
	    
	    String xiazaiFileName=fileName;
	    
	    response.setContentType("application/octet-stream");
	    response.setHeader("content-type", "application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment;fileName=" + xiazaiFileName);
	    response.setHeader("Access-Control-Allow-Origin", "*");

	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    try {
	        File file = new File(filePath, fileName);
	        byte[] buffer = new byte[1024];
	        fis = new FileInputStream(file);
	        bis = new BufferedInputStream(fis);
	        ServletOutputStream os = response.getOutputStream();
	        int i = bis.read(buffer);
	        while (i != -1) {
	            os.write(buffer, 0, i);
	            i = bis.read(buffer);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (bis != null) {
	            try {
	                bis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        if (fis != null) {
	            try {
	                fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return null;

	}
	
	
}
