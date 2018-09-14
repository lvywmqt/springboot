package com.free.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.free.springboot.dto.SupportAddressDTO;
import com.free.springboot.entity.SupportAddress;
import com.free.springboot.repository.SupportAddressRepository;
import com.free.springboot.service.AddressService;
import com.free.springboot.service.ServiceMultiResult;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private SupportAddressRepository supportAddressRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public ServiceMultiResult<SupportAddressDTO> findAllcities() {
		List<SupportAddress> address = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());
		List<SupportAddressDTO> addressDtos = new ArrayList<>();
		for (SupportAddress supportAddress : address) {
			SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);
			addressDtos.add(target);
		}
		return new ServiceMultiResult<>(addressDtos.size(),addressDtos);
	}

}
