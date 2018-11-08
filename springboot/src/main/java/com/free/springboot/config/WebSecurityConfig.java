package com.free.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.free.springboot.security.AuthFilter;
import com.free.springboot.security.AuthProvider;
import com.free.springboot.security.LoginAuthFailHandler;
import com.free.springboot.security.LoginUrlEntryPoint;

@EnableWebMvc
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.authorizeRequests()
			.antMatchers("/admin/login").permitAll()
			.antMatchers("/static/**").permitAll()
			.antMatchers("/user/login").permitAll()
			.antMatchers("/admin/**").permitAll()
			.antMatchers("/user/**").permitAll()
			.antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER").and()
			.formLogin()
			.loginProcessingUrl("/login")
			.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/user/login")
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true).and()
			.exceptionHandling()
			.authenticationEntryPoint(loginUrlEntryPoint())
			.and();

		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
	}

	/**
	 * 自定义认证逻辑
	 * 
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider()).eraseCredentials(true);
	}

	@Bean
	public AuthProvider authProvider() {
		return new AuthProvider();
	}
	
	@Bean
	public LoginUrlEntryPoint loginUrlEntryPoint(){
		return new LoginUrlEntryPoint("/user/login");
	}
	
    @Bean
    public LoginAuthFailHandler authFailHandler() {
        return new LoginAuthFailHandler(loginUrlEntryPoint());
    }
	
    @Bean
    public AuthenticationManager authenticationManager() {
        AuthenticationManager authenticationManager = null;
        try {
            authenticationManager =  super.authenticationManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticationManager;
    }
    
	@Bean
	public AuthFilter authFilter(){
		AuthFilter authFilter = new AuthFilter();
		authFilter.setAuthenticationManager(authenticationManager());
		authFilter.setAuthenticationFailureHandler(authFailHandler());
		return authFilter;
	}
}
