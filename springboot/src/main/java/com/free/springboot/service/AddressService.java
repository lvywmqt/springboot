package com.free.springboot.service;

import java.util.List;
import java.util.Map;

import com.free.springboot.dto.BaiduMapLocation;
import com.free.springboot.dto.SubwayDTO;
import com.free.springboot.dto.SubwayStationDTO;
import com.free.springboot.dto.SupportAddressDTO;
import com.free.springboot.entity.SupportAddress.Level;


public interface AddressService {
	
	ServiceMultiResult<SupportAddressDTO> findAllcities();

	ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityEnName);

	Map<Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

	List<SubwayDTO> findAllSubwayByCity(String cityEnName);

	List<SubwayStationDTO> findAllStationBySubway(Long subwayId);

	ServiceResult<SubwayDTO> findSubway(Long subwayLineId);

	ServiceResult<SubwayStationDTO> findSubwayStation(Long subwayStationId);

	ServiceResult<SupportAddressDTO> findByCity(String cityEnName);

	ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city, String address);
}
