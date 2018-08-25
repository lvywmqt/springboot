package com.free.springboot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.free.springboot.base.ApiResponse;
import com.free.springboot.service.impl.AliossService;

@Controller
public class OSSUploadController {

	// 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" };

    /**
     * oss上传图片
     * @param file
     * @param request
     * @return
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     */

    
    @RequestMapping(value = "toFileUpload")
    public String fileUpload() {
		return "fileUpload";
    }
    
    
    @RequestMapping(value="/upload",method=RequestMethod.POST)  
    public String upload(MultipartFile file,HttpServletRequest request) throws IOException{
        
    	String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName = file.getOriginalFilename();//获取到上传文件的名字
        //System.out.println(file.getName()+"2222");获取到file
        //file.getSize();获取到上传文件的大小
        File dir = new File(path,fileName);
        System.out.println(path);
        System.out.println(file.getSize());
        if(!dir.exists()){  
            dir.mkdirs();  
        }  
        //MultipartFile自带的解析方法  
        file.transferTo(dir);  
        return "/upload"+"/"+fileName;  
    }  

}
