package com.free.springboot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.free.springboot.dto.BaiduMapLocation;
import com.free.springboot.service.AddressService;
import com.free.springboot.service.ServiceResult;

public class AddressServiceTests extends SpringbootApplicationTests {

	@Autowired
    private AddressService addressService;
	
	@Test
    public void testGetMapLocation() {
        String city = "北京";
        String address = "北京市昌平区巩华家园1号楼2单元";
        ServiceResult<BaiduMapLocation> serviceResult = addressService.getBaiduMapLocation(city, address);
        BaiduMapLocation result = serviceResult.getResult();
       
        System.out.println(result.getLatitude());
        System.out.println(result.getLongitude());
    }
	
}
