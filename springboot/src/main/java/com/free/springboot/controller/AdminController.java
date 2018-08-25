package com.free.springboot.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.free.springboot.base.ApiResponse;
import com.free.springboot.form.HouseForm;
import com.free.springboot.service.HouseService;
import com.free.springboot.service.impl.AliossService;
import  com.free.springboot.form.PhotoForm;

@Controller
public class AdminController {
	
	private List<PhotoForm> listForm;
	@Autowired
	private AliossService aliossService;
	@Autowired
	private HouseService houseService; 
	
	@Value("${AccessKeyID}")
	private String AccessKeyID;
	@Value("${AccessKeySecret}")
	private String AccessKeySecret;
	
	@GetMapping("/admin/welcome")
	public String welcomePage() {
		return "admin/welcome";
	}

	@GetMapping("/admin/center")
	public String centerPage() {
		return "admin/center";
	}
	
	@GetMapping("/admin/login")
	public String adminLogin() {
		return "admin/login";
	}
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
    @RequestMapping(value = "ossFileUpload" ,method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
    @ResponseBody
    public ApiResponse ossFileUpload(@RequestParam("file")MultipartFile file,HttpServletRequest request) throws OSSException, ClientException, IOException{
    	String aliOss = "https://9758xunwu.oss-cn-beijing.aliyuncs.com/";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    	 String fileName = aliossService.aliOss(file,AccessKeyID ,AccessKeySecret);
    	 PhotoForm photoForm = new PhotoForm();
    	if(fileName != null){
    		String path = aliOss + fileName ;
    		photoForm.setPath(path);
    		ApiResponse.ofSuccess(path);
    		listForm.add(photoForm);
    	}
    	return ApiResponse.ofMessage(500, "上传异常");
    }
	
    /**
     * 新增房源功能页
     * @return
     */
    @GetMapping("admin/add/house")
    public String addHousePage() {
        return "admin/house-add";
    }
    
    /**
     * 新增房源接口
     * @param houseForm
     * @param bindingResult
     * @return
     */
    @PostMapping("admin/add/house")
    @ResponseBody
    public ApiResponse addHouse(@Valid @ModelAttribute("form-house-add") HouseForm houseForm, BindingResult bindingResult) {
    	houseForm.setPhotos(listForm);
    	listForm.clear();
		return null;

    }
}
