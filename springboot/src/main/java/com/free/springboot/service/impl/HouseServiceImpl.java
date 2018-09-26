package com.free.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.free.springboot.dto.HouseDTO;
import com.free.springboot.entity.House;
import com.free.springboot.form.HouseForm;
import com.free.springboot.repository.HouseRepository;
import com.free.springboot.service.HouseService;
import com.free.springboot.service.ServiceResult;

@Service
public class HouseServiceImpl implements HouseService {

	@Autowired
	private HouseRepository houseRepository;

	@Override
	public void saveHousePic(String aliOss) {
		
		//houseRepository.save(entity);
	}

	@Override
	public ServiceResult<HouseDTO> save(HouseForm houseForm) {
		
		return null;
	}
	
}
