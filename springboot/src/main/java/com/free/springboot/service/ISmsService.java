package com.free.springboot.service;

/**
 * 验证码服务
 * @author user
 *
 */
public interface ISmsService {

	ServiceResult<String> SendSms(String telephone);
	
	String getSms(String telephone);
	
	ServiceResult deleteSms(String telephone);
}
