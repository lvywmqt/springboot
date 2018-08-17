package com.free.springboot.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 登录拦截器,根据不同的url进入与之对应的login页面
 * @author user
 *
 */
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	private Map<String, String> authEntryPointMap;

	public LoginUrlEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
		authEntryPointMap = new HashMap<>();
		authEntryPointMap.put("/user/**", "/user/login");
		authEntryPointMap.put("/admin/**", "/admin/login");
	}
	
	/**
	 * 根据请求跳转指定页面,父类默认loginFormUrl
	 */
	@Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		String requestURI = request.getRequestURI();
		System.out.println(request.getRequestURL());
		System.out.println(request.getRequestURI());
		String uri = requestURI.replace(request.getContextPath(), "");

		for (Map.Entry<String, String> authEntry : authEntryPointMap.entrySet()) {
			if(pathMatcher.match(authEntry.getKey(), uri)){
				return authEntry.getValue();
			}
		}
		return super.determineUrlToUseForThisRequest(request, response, exception);
	}
	
}
