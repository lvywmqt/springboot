package com.free.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.free.springboot.entity.User;
import com.free.springboot.service.UserService;

/**
 * 自定义认证实现
 * Created by 2018/8/15.
 */
public class AuthProvider implements AuthenticationProvider {

	 @Autowired
	 private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = userService.findUserByName(userName ,password);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("authError"); 
        }
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> arg0) {
		
		return true;
	}

}
