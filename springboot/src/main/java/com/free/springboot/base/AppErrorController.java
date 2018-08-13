package com.free.springboot.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class AppErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";
	private ErrorAttributes errorAttributes;
	
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@Autowired
	public AppErrorController(ErrorAttributes errorAttributes){
		this.errorAttributes = errorAttributes;
	}
	
	/**
	 * web页面的error
	 */
	@RequestMapping(value = "ERROR_PATH" , produces = "text/html")
	public String errorPageHandler(HttpServletRequest request){
		int status = getStatus(request);
		
		return "error";
	}
	
	/**
     * 除Web页面外的错误处理，比如Json/XML等
     */
	public ApiResponse errorApiHandler(HttpServletRequest request){
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		Map<String, Object> map = errorAttributes.getErrorAttributes(requestAttributes, false);
		int status = getStatus(request);
		
		return ApiResponse.ofMessage(status, String.valueOf(map.getOrDefault("message", "error")));
	}
	
	private int getStatus(HttpServletRequest request){
		return (Integer) request.getAttribute("javax.servlet.error.status_code");
		
	}
	
}
