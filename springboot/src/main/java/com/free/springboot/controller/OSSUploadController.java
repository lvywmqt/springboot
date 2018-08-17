package com.free.springboot.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.free.springboot.base.ApiResponse;

@Controller
public class OSSUploadController {
	
	@Value("${AccessKeyID}")
	private String AccessKeyID;
	@Value("${AccessKeySecret}")
	private String AccessKeySecret;
	
	@RequestMapping(value = "ossFileUpload" ,method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	public ApiResponse fileUpload(@RequestParam("file")MultipartFile file) throws IOException{
		String endpoint = "http://9758xunwu.oss-cn-beijing.aliyuncs.com";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
		String accessKeyId = AccessKeyID;
		String accessKeySecret = AccessKeySecret;
		return null;
		
	}
	//
}
