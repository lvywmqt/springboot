package com.free.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Created by 瓦力.
 */
@Controller
public class UserController {

    @GetMapping("/user/welcome")
    public String loginPage() {
        return "user/welcome";
    }

    @GetMapping("/user/center")
    public String centerPage() {
        return "user/center";
    }

    @GetMapping("/user/login")
    public String adminLogin() {
        return "user/login";
    }
}

