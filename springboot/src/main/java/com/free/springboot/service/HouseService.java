package com.free.springboot.service;

import com.free.springboot.dto.HouseDTO;
import com.free.springboot.form.DatatableSearch;
import com.free.springboot.form.HouseForm;
import com.free.springboot.form.RentSearch;

public interface HouseService {

	void saveHousePic(String aliOss);

	ServiceResult<HouseDTO> save(HouseForm houseForm);

	ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody);

	ServiceResult<HouseDTO> findHouseOne(Long id);

	ServiceResult update(HouseForm houseForm);

	ServiceResult removePhoto(Long id);

	ServiceResult removeTag(Long houseId, String tag);

	ServiceResult addTag(Long houseId, String tag);

	ServiceResult updateCover(Long coverId, Long targetId);

	ServiceResult updateStatus(Long id, int value);

	ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);


}
