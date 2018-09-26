package com.free.springboot.service;

import com.free.springboot.dto.HouseDTO;
import com.free.springboot.form.HouseForm;

public interface HouseService {

	void saveHousePic(String aliOss);

	ServiceResult<HouseDTO> save(HouseForm houseForm);

}
