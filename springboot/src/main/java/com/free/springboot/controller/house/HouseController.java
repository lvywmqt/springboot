package com.free.springboot.controller.house;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.free.springboot.base.ApiResponse;
import com.free.springboot.base.RentValueBlock;
import com.free.springboot.dto.SupportAddressDTO;
import com.free.springboot.dto.UserDTO;
import com.free.springboot.entity.SupportAddress;
import com.free.springboot.entity.SupportAddress.Level;
import com.free.springboot.form.RentSearch;
import com.free.springboot.service.AddressService;
import com.free.springboot.service.HouseService;
import com.free.springboot.service.ServiceMultiResult;
import com.free.springboot.service.ServiceResult;
import com.free.springboot.service.UserService;
import com.free.springboot.service.serch.SearchService;
import com.free.springboot.dto.HouseBucketDTO;
import com.free.springboot.dto.HouseDTO;
import com.free.springboot.dto.SubwayDTO;
import com.free.springboot.dto.SubwayStationDTO;

@Controller
public class HouseController {

	@Autowired
	private AddressService addressService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private UserService userService;
	@Autowired
	private SearchService searchService;
	 /**
     * 获取支持城市列表
     * @return
     */
	@GetMapping("address/support/cities")
	@ResponseBody
	public ApiResponse getSupportCities(){
		ServiceMultiResult<SupportAddressDTO> result = addressService.findAllcities();
		if(result.getResultSize() == 0){
			ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
		}
		return ApiResponse.ofSuccess(result.getResult());
	}

	 /**
     * 获取对应城市支持区域列表
     * @param cityEnName
     * @return
     */
    @GetMapping("address/support/regions")
    @ResponseBody
    public ApiResponse getSupportRegions(@RequestParam(name = "city_name") String cityEnName) {
        ServiceMultiResult<SupportAddressDTO> addressResult = addressService.findAllRegionsByCityName(cityEnName);
        if (addressResult.getResult() == null || addressResult.getTotal() < 1) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_FOUND);
        }
        return ApiResponse.ofSuccess(addressResult.getResult());
    }
    
    /**
     * 获取具体城市所支持的地铁线路
     * @param cityEnName
     * @return
     */
    @GetMapping("address/support/subway/line")
    @ResponseBody
    public ApiResponse getSupportSubwayLine(@RequestParam(name = "city_name") String cityEnName) {
        List<SubwayDTO> subways = addressService.findAllSubwayByCity(cityEnName);
        if (subways.isEmpty()) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_FOUND);
        }

        return ApiResponse.ofSuccess(subways);
    }

    /**
     * 获取对应地铁线路所支持的地铁站点
     * @param subwayId
     * @return
     */
    @GetMapping("address/support/subway/station")
    @ResponseBody
    public ApiResponse getSupportSubwayStation(@RequestParam(name = "subway_id") Long subwayId) {
        List<SubwayStationDTO> stationDTOS = addressService.findAllStationBySubway(subwayId);
        if (stationDTOS.isEmpty()) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_FOUND);
        }

        return ApiResponse.ofSuccess(stationDTOS);
    }
    
    @GetMapping("rent/house")
    public String rentHousePage(@ModelAttribute RentSearch rentSearch,
                                Model model, HttpSession session,
                                RedirectAttributes redirectAttributes) {
				
    	if(rentSearch.getCityEnName() == null){
    		String cityEnNameInSession = (String) session.getAttribute("cityEnName");
    		if(cityEnNameInSession == null){
    			redirectAttributes.addAttribute("msg", "must_chose_city");
    			return "redirect:/index";
    		}
    		else {
    			rentSearch.setCityEnName(cityEnNameInSession);
    		}
    	}else{
    		session.setAttribute("cityEnName", rentSearch.getCityEnName());
    	}
    	ServiceResult<SupportAddressDTO> city = addressService.findByCity(rentSearch.getCityEnName());
    	model.addAttribute("currentCity", city.getResult());
    	if(!city.isSuccess()){
    		redirectAttributes.addAttribute("msg", "must_chose_city");
    		return "redirect:/index";
    	}
    	
   		ServiceMultiResult<HouseDTO> query = houseService.query(rentSearch);
        model.addAttribute("total", query.getTotal());
        model.addAttribute("houses", query.getResult());
    	ServiceMultiResult<SupportAddressDTO> addressResult = addressService.findAllRegionsByCityName(rentSearch.getCityEnName());
    	if(rentSearch.getRegionEnName() == null){
    		rentSearch.setRegionEnName("*");
    	}
    	 model.addAttribute("searchBody", rentSearch);
         model.addAttribute("regions", addressResult.getResult());

         model.addAttribute("priceBlocks", RentValueBlock.PRICE_BLOCK);
         model.addAttribute("areaBlocks", RentValueBlock.AREA_BLOCK);

         model.addAttribute("currentPriceBlock", RentValueBlock.matchPrice(rentSearch.getPriceBlock()));
         model.addAttribute("currentAreaBlock", RentValueBlock.matchArea(rentSearch.getAreaBlock()));

         return "rent-list";
    	
    }
    
	@GetMapping("rent/house/show/{id}")
	public String show(@PathVariable(value = "id") Long houseId,
	                       Model model) {
	 
        if (houseId <= 0) {
            return "404";
        }
        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(houseId);
        if (!serviceResult.isSuccess()) {
        	return "404";
        }
        HouseDTO houseDTO = serviceResult.getResult();
        Map<Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());
        SupportAddressDTO city = addressMap.get(SupportAddress.Level.CITY);
        SupportAddressDTO region = addressMap.get(SupportAddress.Level.REGION);
        model.addAttribute("city", city);
        model.addAttribute("region", region);
        
        ServiceResult<UserDTO> userDTOServiceResult = userService.findById(houseDTO.getAdminId());
        model.addAttribute("agent", userDTOServiceResult.getResult());
        model.addAttribute("house", houseDTO);
        
       /* ServiceResult<Long> aggResult = searchService.aggregateDistrictHouse(city.getEnName(), region.getEnName(), houseDTO.getDistrict());
        model.addAttribute("houseCountInDistrict", aggResult.getResult());*/
        return "house-detail";
	}
	
	/**
     * 自动补全接口
     */
    @GetMapping("rent/house/autocomplete")
    @ResponseBody
	public ApiResponse autocomplete(String prefix){
		if(prefix.isEmpty()){
			return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
		}
		//ServiceResult<List<String>> result = searchService.suggest(prefix);
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		return ApiResponse.ofSuccess(list);
	}
    Logger logger = LoggerFactory.getLogger(HouseController.class);
	
    @GetMapping("rent/house/map")
    public String rentMapPage(
    		@RequestParam(value = "cityEnName") String cityEnName ,
    		Model model ,HttpSession session, RedirectAttributes redirectAttributes){
    	session.setAttribute("enName" ,cityEnName);
    	ServiceResult<SupportAddressDTO> city = addressService.findByCity(cityEnName);
    	logger.debug(city.getResult().getCnName());
    	model.addAttribute("city" ,city.getResult());
    	ServiceMultiResult<SupportAddressDTO> regions = addressService.findAllRegionsByCityName(cityEnName);
    	ServiceMultiResult<HouseBucketDTO> serviceResult = searchService.mapAggregate(cityEnName);
    	model.addAttribute("total" ,serviceResult.getTotal());
    	model.addAttribute("regions" ,regions.getResult());
    	model.addAttribute("aggData", serviceResult.getResult());
    	return "rent-map";
    }
}
