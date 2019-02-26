package com.free.springboot.service.serch;

import java.util.List;

import com.free.springboot.dto.HouseBucketDTO;
import com.free.springboot.dto.HouseDTO;
import com.free.springboot.form.RentSearch;
import com.free.springboot.service.ServiceMultiResult;
import com.free.springboot.service.ServiceResult;

public interface SearchService {
	
	void index(Long houseId); 
	
	void remove(Long houseId); 
	
	ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);

	ServiceResult<List<String>> suggest(String prefix);

	ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName);

}
