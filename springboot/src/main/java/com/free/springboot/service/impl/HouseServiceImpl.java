package com.free.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.free.springboot.entity.House;
import com.free.springboot.repository.HouseRepository;
import com.free.springboot.service.HouseService;

@Service
public class HouseServiceImpl implements HouseService {

	@Autowired
	private HouseRepository houseRepository;

	@Override
	public void saveHousePic(String aliOss) {
		
		//houseRepository.save(entity);
	}
	
}
