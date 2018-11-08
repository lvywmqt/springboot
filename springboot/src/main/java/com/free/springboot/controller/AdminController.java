package com.free.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.free.springboot.base.ApiDataTableResponse;
import com.free.springboot.base.ApiResponse;
import com.free.springboot.base.HouseOperation;
import com.free.springboot.base.HouseStatus;
import com.free.springboot.dto.HouseDTO;
import com.free.springboot.dto.HouseDetailDTO;
import com.free.springboot.dto.SubwayDTO;
import com.free.springboot.dto.SubwayStationDTO;
import com.free.springboot.dto.SupportAddressDTO;
import com.free.springboot.entity.SupportAddress;
import com.free.springboot.entity.SupportAddress.Level;
import com.free.springboot.form.DatatableSearch;
import com.free.springboot.form.HouseForm;
import com.free.springboot.form.PhotoForm;
import com.free.springboot.service.AddressService;
import com.free.springboot.service.HouseService;
import com.free.springboot.service.ServiceMultiResult;
import com.free.springboot.service.ServiceResult;
import com.free.springboot.service.impl.AliossService;
import com.google.common.base.Strings;

@Controller
public class AdminController {

	private List<PhotoForm> listForm;
	@Autowired
	private AliossService aliossService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private AddressService addressService;

	@Value("${AccessKeyID}")
	private String AccessKeyID;
	@Value("${AccessKeySecret}")
	private String AccessKeySecret;

	@GetMapping("/admin/welcome")
	public String welcomePage() {
		return "admin/welcome";
	}

	@GetMapping("/admin/center")
	public String centerPage() {
		return "admin/center";
	}

	@GetMapping("/admin/login")
	public String adminLogin() {
		return "admin/login";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	/**
	 * 新增房源功能页
	 * 
	 * @return
	 */
	@GetMapping("admin/add/house")
	public String addHousePage() {
		return "admin/house-add";
	}
	
	@GetMapping("admin/house/subscribe")
	public String houseSubscribe() {
		return "admin/subscribe";
	}

	@RequestMapping(value = "ossFileUpload", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public void ossFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws OSSException, ClientException, IOException {
		String aliOss = "https://9758xunwu.oss-cn-beijing.aliyuncs.com/";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录
		// https://ram.console.aliyun.com 创建。
		String fileName = aliossService.aliOss(file, AccessKeyID, AccessKeySecret);
		PhotoForm photoForm = new PhotoForm();
		if (fileName != null) {
			listForm = new ArrayList<>();
			String path = aliOss + fileName;
			photoForm.setPath(path);
			listForm.add(photoForm);
			response.getWriter().print(path);
		}
	}

	/**
	 * 新增房源接口
	 * 
	 * @param houseForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("admin/add/house")
	@ResponseBody
	public ApiResponse addHouse(@Valid @ModelAttribute("form-house-add") HouseForm houseForm,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ApiResponse(HttpStatus.BAD_REQUEST.value(),
					bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
		}
		List<PhotoForm> listForm = new ArrayList<>();
		PhotoForm photoForm = new PhotoForm();
		photoForm.setPath("https://9758xunwu.oss-cn-beijing.aliyuncs.com/2018101004260853507514.png");
		listForm.add(photoForm);
		houseForm.setPhotos(listForm);
		if (houseForm.getPhotos() == null) {
			return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), "必须上传图片");
		}

		Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService
				.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());
		if (addressMap.keySet().size() != 2) {
			return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
		}

		ServiceResult<HouseDTO> result = houseService.save(houseForm);
		if (result.isSuccess()) {
			return ApiResponse.ofSuccess(result.getResult());
		}

		return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAM);

	}

	/**
	 * 去房源列表页
	 * 
	 * @return
	 */
	@GetMapping("admin/house/list")
	public String houseListPage() {
		return "admin/house-list";
	}

	/**
	 * ApiDataTableResponse 为jquery datatable 返回结构
	 * 
	 * @param searchBody
	 * @return
	 */
	@PostMapping("admin/houses")
	@ResponseBody
	public ApiDataTableResponse houses(@ModelAttribute DatatableSearch searchBody) {
		ServiceMultiResult<HouseDTO> result = houseService.adminQuery(searchBody);
		ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
		response.setData(result.getResult());
		response.setRecordsFiltered(result.getTotal());
		response.setRecordsTotal(result.getTotal());

		response.setDraw(searchBody.getDraw());
		return response;
	}

	/**
	 * 房源信息编辑页
	 * 
	 * @return
	 */
	@GetMapping("admin/house/edit")
	public String houseEditPage(@RequestParam(value = "id") Long id, Model model) {
		if (id == null || id < 1) {
			return "404";
		}
		ServiceResult<HouseDTO> serviceResult = houseService.findHouseOne(id);
		if (!serviceResult.isSuccess()) {
			return "404";
		}
		HouseDTO result = serviceResult.getResult();
		model.addAttribute("house", result);

		Map<Level, SupportAddressDTO> addressMap = 
				addressService.findCityAndRegion(result.getCityEnName(), result.getRegionEnName());
		model.addAttribute("city", addressMap.get(SupportAddress.Level.CITY));
		model.addAttribute("region", addressMap.get(SupportAddress.Level.REGION));

		HouseDetailDTO detailDTO = result.getHouseDetail();
		ServiceResult<SubwayDTO> subwayServiceResult = addressService.findSubway(detailDTO.getSubwayLineId());
		if (subwayServiceResult.isSuccess()) {
			model.addAttribute("subway", subwayServiceResult.getResult());
		}

		ServiceResult<SubwayStationDTO> subwayStationServiceResult = addressService
				.findSubwayStation(detailDTO.getSubwayStationId());
		if (subwayStationServiceResult.isSuccess()) {
			model.addAttribute("station", subwayStationServiceResult.getResult());
		}

		return "admin/house-edit";
	}

	/**
	 * 编辑接口
	 */
	@PostMapping("admin/house/edit")
	@ResponseBody
	public ApiResponse saveHouse(@Valid @ModelAttribute("form-house-edit")HouseForm houseForm ,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ApiResponse(HttpStatus.BAD_REQUEST.value(),
					bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
		}
		Map<Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());
		if(addressMap.keySet().size() != 2 ){
			 return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAM);
		}
		ServiceResult result = houseService.update(houseForm);
        if (result.isSuccess()) {
            return ApiResponse.ofSuccess(null);
        }
        ApiResponse response = ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        response.setMessage(result.getMessage());
        return response;
	}
	
	  /**
     * 移除图片接口
     * @param id
     * @return
     */
    @DeleteMapping("admin/house/photo")
    @ResponseBody
    public ApiResponse removeHousePhoto(@RequestParam(value = "id") Long id) {
        ServiceResult result = this.houseService.removePhoto(id);

        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 修改封面接口
     * @param coverId
     * @param targetId
     * @return
     */
    @PostMapping("admin/house/cover")
    @ResponseBody
    public ApiResponse updateCover(@RequestParam(value = "cover_id") Long coverId,
                                   @RequestParam(value = "target_id") Long targetId) {
        ServiceResult result = this.houseService.updateCover(coverId, targetId);

        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 增加标签接口
     * @param houseId
     * @param tag
     * @return
     */
    @PostMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse addHouseTag(@RequestParam(value = "house_id") Long houseId,
                                   @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || Strings.isNullOrEmpty(tag)) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.addTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 移除标签接口
     * @param houseId
     * @param tag
     * @return
     */
    @DeleteMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse removeHouseTag(@RequestParam(value = "house_id") Long houseId,
                                      @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || Strings.isNullOrEmpty(tag)) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.removeTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
        
    }
    
    /**
     * 审核接口
     * @param id
     * @param operation
     * @return
     */
    @PutMapping("admin/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiResponse operateHouse(@PathVariable(value = "id") Long id,
                                    @PathVariable(value = "operation") int operation) {
		if (id <= 0) {
			return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
		}
		ServiceResult result = null;
		switch (operation) {
			case HouseOperation.RENT:
				result = houseService.updateStatus(id, HouseStatus.RENTED.getValue());
				break;
			case HouseOperation.PULL_OUT:
				result = houseService.updateStatus(id, HouseStatus.NOT_AUDITED.getValue());
				break;
			case HouseOperation.DELETE:
				result = houseService.updateStatus(id, HouseStatus.DELETED.getValue());
				break;
			default:
				result = houseService.updateStatus(id, HouseStatus.PASSES.getValue());
		}
		if (result.isSuccess()) {
			return ApiResponse.ofSuccess(null);
		}
		return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
	}

    @GetMapping("admin/house/subscribe/list")
    @ResponseBody
    public ApiResponse subscribeList(@RequestParam(value = "draw") int draw,
                                     @RequestParam(value = "start") int start,
                                     @RequestParam(value = "length") int size) {
		
    	return null;
    }
    
}
