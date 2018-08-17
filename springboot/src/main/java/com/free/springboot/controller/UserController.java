package com.free.springboot.controller;

import com.free.springboot.entity.User;
import com.free.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Created by 瓦力.
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

    @GetMapping("/user/welcome")
    public String loginPage() {
        return "user/welcome";
    }

    @GetMapping("/user/center")
    public String centerPage() {
        return "user/center";
    }

    @GetMapping("/user/login")
    public String userLogin() {
        return "user/login";
    }
    
    @PostMapping("/login")
    public void login(
    		@RequestParam("username")String userName,
    		@RequestParam("password")String password){
    	User user = userService.findUserByName(userName ,password);
    }
}

