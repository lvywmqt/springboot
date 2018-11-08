package com.free.springboot.service.impl;

import org.springframework.stereotype.Service;

import com.free.springboot.service.ISmsService;
import com.free.springboot.service.ServiceResult;

@Service
public class SmsServiceImpl implements ISmsService {

	@Override
	public ServiceResult<String> SendSms(String telephone) {
		return ServiceResult.of("123456");
	}

	@Override
	public String getSms(String telephone) {
		return "123456";
	}

	@Override
	public ServiceResult deleteSms(String telephone) {
		return null;
	}
	
}
