package com.free.springboot.security;

import java.util.Objects;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.free.springboot.base.LoginUserUtil;
import com.free.springboot.entity.User;
import com.free.springboot.service.ISmsService;
import com.free.springboot.service.UserService;
import com.free.springboot.service.impl.SmsServiceImpl;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private UserService userService;
	@Autowired
	private ISmsService smsService;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		//获取登录用户名
		String name = obtainUsername(request);
		//判断该用户是否已登录
		if(!Strings.isNullOrEmpty(name)){
			request.setAttribute("username", name);
			return super.attemptAuthentication(request, response);
		}
		String telephone = request.getParameter("telephone");
		if(Strings.isNullOrEmpty(telephone) || LoginUserUtil.checkTelephone(telephone)){
			throw new BadCredentialsException("Wrong Telephone Number");
		}
		
		User user = userService.findUserByTelephone(telephone);
		String inputCode = request.getParameter("smsCode");
		String sessionCode = smsService.getSms(telephone);
		if(Objects.equals(inputCode, sessionCode)){
			if(user == null){
				user = userService.addUserByPhone(telephone);
			}
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}else{
			throw new BadCredentialsException("smsCodeError");
		}
	}

	
	
}
