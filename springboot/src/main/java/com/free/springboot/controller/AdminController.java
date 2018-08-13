package com.free.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	
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
}
