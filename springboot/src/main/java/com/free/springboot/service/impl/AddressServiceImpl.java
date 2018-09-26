package com.free.springboot.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.free.springboot.dto.SupportAddressDTO;
import com.free.springboot.entity.Subway;
import com.free.springboot.entity.SubwayStation;
import com.free.springboot.entity.SupportAddress;
import com.free.springboot.repository.SubwayRepository;
import com.free.springboot.repository.SubwayStationRepository;
import com.free.springboot.repository.SupportAddressRepository;
import com.free.springboot.service.AddressService;
import com.free.springboot.dto.SubwayDTO;
import com.free.springboot.dto.SubwayStationDTO;
import com.free.springboot.service.ServiceMultiResult;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private SupportAddressRepository supportAddressRepository;
	@Autowired
	private SubwayRepository subwayRepository;
	@Autowired
	private SubwayStationRepository subwayStationRepository;
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

	@Override
	public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName ,String regionEnName) {
		Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();
		SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY
	             .getValue());
		SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName, city.getEnName());
		result.put(SupportAddress.Level.CITY, modelMapper.map(city, SupportAddressDTO.class));
		result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDTO.class));
		return result;
	}
    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityName) {
        if (cityName == null) {
            return new ServiceMultiResult<>(0, null);
        }

        List<SupportAddressDTO> result = new ArrayList<>();

        List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION
                .getValue(), cityName);
        for (SupportAddress region : regions) {
            result.add(modelMapper.map(region, SupportAddressDTO.class));
        }
        return new ServiceMultiResult<>(regions.size(), result);
    }

	@Override
	public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        List<SubwayDTO> result = new ArrayList<>();
        List<Subway> subways = subwayRepository.findAllByCityEnName(cityEnName);
        if (subways.isEmpty()) {
            return result;
        }

        subways.forEach(subway -> result.add(modelMapper.map(subway, SubwayDTO.class)));
        return result;
	}

	@Override
	public List<SubwayStationDTO> findAllStationBySubway(Long subwayId) {
		List<SubwayStationDTO> result = new ArrayList<>();
		List<SubwayStation> stations = subwayStationRepository.findAllBySubwayId(subwayId);
		if (stations.isEmpty()) {
		    return result;
		}
		stations.forEach(station -> result.add(modelMapper.map(station, SubwayStationDTO.class)));
		return result;
	}

}
