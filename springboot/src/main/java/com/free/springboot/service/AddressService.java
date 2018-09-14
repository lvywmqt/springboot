package com.free.springboot.service;

import java.util.List;

import com.free.springboot.dto.SupportAddressDTO;

public interface AddressService {
	
	ServiceMultiResult<SupportAddressDTO> findAllcities();

}
