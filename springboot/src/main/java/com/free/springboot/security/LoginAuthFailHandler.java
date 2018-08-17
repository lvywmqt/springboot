package com.free.springboot.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 登录验证失败处理器
 * Created by 2018/08/15.
 */
public class LoginAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private LoginUrlEntryPoint urlEntryPoint;
	
    public LoginAuthFailHandler(LoginUrlEntryPoint urlEntryPoint) {
        this.urlEntryPoint = urlEntryPoint;
    }

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String fialurl = urlEntryPoint.determineUrlToUseForThisRequest(request, response, exception);
		fialurl += "?" + exception.getMessage();
        super.setDefaultFailureUrl(fialurl);
		super.onAuthenticationFailure(request, response, exception);
	}

}
