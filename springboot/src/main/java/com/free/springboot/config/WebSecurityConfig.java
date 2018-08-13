package com.free.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/admin/login").permitAll()
		.antMatchers("/static/**").permitAll()
		.antMatchers("/user/login").permitAll()
		.antMatchers("/admin/**").permitAll()
		.antMatchers("/user/**").permitAll()
		.antMatchers("/api/user/**").hasAnyRole("ADMIN","USER")
		.and()
		.formLogin()
		.loginProcessingUrl("/login").and();
		
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
	}
	
	/**
	 * 自定义认证逻辑
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN").and();
	}
}
