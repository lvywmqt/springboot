package com.free.springboot.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.free.springboot.dto.BaiduMapLocation;
import com.free.springboot.dto.SubwayDTO;
import com.free.springboot.dto.SubwayStationDTO;
import com.free.springboot.service.ServiceMultiResult;
import com.free.springboot.service.ServiceResult;

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
    
	private static final Logger logger = LoggerFactory.getLogger(AddressService.class);
	private static final String BAIDU_MAP_GEOCONV_API = "http://api.map.baidu.com/geocoder/v2/?";
	private static final String BAIDU_MAP_KEY = "0Q8jK44kQspnY24E2SMYNI5AZnciop3s";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
	public ServiceMultiResult<SupportAddressDTO> findAllcities() {
		List<SupportAddress> address = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());
		List<SupportAddressDTO> addressDtos = new ArrayList<>();
		for (SupportAddress supportAddress : address) {
			SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);
			addressDtos.add(target);
		}
		return new ServiceMultiResult<>(addressDtos.size(), addressDtos);
	}

	@Override
	public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
		Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();
		SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName,
				SupportAddress.Level.CITY.getValue());
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

		List<SupportAddress> regions = supportAddressRepository
				.findAllByLevelAndBelongTo(SupportAddress.Level.REGION.getValue(), cityName);
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

	@Override
	public ServiceResult<SubwayDTO> findSubway(Long subwayId) {
		if (subwayId == null) {
			return ServiceResult.notFound();
		}
		Subway subway = subwayRepository.findOne(subwayId);
		if (subway == null) {
			return ServiceResult.notFound();
		}
		return ServiceResult.of(modelMapper.map(subway, SubwayDTO.class));
	}

	@Override
	public ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId) {
		if (stationId == null) {
			return ServiceResult.notFound();
		}
		SubwayStation station = subwayStationRepository.findOne(stationId);
		if (station == null) {
			return ServiceResult.notFound();
		}
		return ServiceResult.of(modelMapper.map(station, SubwayStationDTO.class));
	}

	@Override
	public ServiceResult<SupportAddressDTO> findByCity(String cityEnName) {
        if (cityEnName == null) {
            return ServiceResult.notFound();
        }
        SupportAddress supportAddress = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
        if (supportAddress == null) {
            return ServiceResult.notFound();
        }
        SupportAddressDTO result = modelMapper.map(supportAddress, SupportAddressDTO.class);
        return ServiceResult.of(result);
	}

	@Override
	public ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city, String address) {
		String encodeAddress;
		String encodeCity;
		try {
			encodeAddress = URLEncoder.encode(address ,"UTF-8");
			encodeCity = URLEncoder.encode(city ,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			 logger.error("Error to encode house address", e);
			 return new ServiceResult<>(false, "Error to encode hosue address");
		}
		HttpClient httpClient = HttpClients.createDefault();
		StringBuilder sb = new StringBuilder(BAIDU_MAP_GEOCONV_API);
		sb.append("address=").append(encodeAddress).append("&city=")
			.append(encodeCity).append("&output=json&").append("ak=").append(BAIDU_MAP_KEY);
		HttpGet get = new HttpGet(sb.toString());
		try {
			HttpResponse response = httpClient.execute(get);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				 return new ServiceResult<BaiduMapLocation>(false, "Can not get baidu map location");
			}
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			JsonNode jsonNode = MAPPER.readTree(result);
			int status = jsonNode.get("status").asInt();
			if(status != 0){
				return new ServiceResult<BaiduMapLocation>(false, "Error to get map location for status: " + status);
			}{
				BaiduMapLocation baiduMapLocation = new BaiduMapLocation();
				JsonNode location = jsonNode.get("result").get("location");
				baiduMapLocation.setLatitude(location.get("lat").asDouble());
				baiduMapLocation.setLongitude(location.get("lng").asDouble());
				return ServiceResult.of(baiduMapLocation);
			}
		} catch (Exception e) {
			logger.error("Error to fetch baidumap api", e);
            return new ServiceResult<BaiduMapLocation>(false, "Error to fetch baidumap api");
		}
	}

}
