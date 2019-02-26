package com.free.springboot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.free.springboot.base.HouseSort;
import com.free.springboot.base.HouseStatus;
import com.free.springboot.base.LoginUserUtil;
import com.free.springboot.dto.HouseDTO;
import com.free.springboot.dto.HouseDetailDTO;
import com.free.springboot.dto.HousePictureDTO;
import com.free.springboot.entity.House;
import com.free.springboot.entity.HouseDetail;
import com.free.springboot.entity.HousePicture;
import com.free.springboot.entity.HouseTag;
import com.free.springboot.entity.Subway;
import com.free.springboot.entity.SubwayStation;
import com.free.springboot.form.DatatableSearch;
import com.free.springboot.form.HouseForm;
import com.free.springboot.form.PhotoForm;
import com.free.springboot.form.RentSearch;
import com.free.springboot.repository.HouseDetailRepository;
import com.free.springboot.repository.HousePictureRepository;
import com.free.springboot.repository.HouseRepository;
import com.free.springboot.repository.HouseTagRepository;
import com.free.springboot.repository.SubwayRepository;
import com.free.springboot.repository.SubwayStationRepository;
import com.free.springboot.service.HouseService;
import com.free.springboot.service.ServiceMultiResult;
import com.free.springboot.service.ServiceResult;
import com.free.springboot.service.serch.SearchService;

@Service
public class HouseServiceImpl implements HouseService {

	@Autowired
	private HouseRepository houseRepository;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private SubwayRepository subwayRepository;
	@Autowired
	private SubwayStationRepository subwayStationRepository;
	@Autowired
	private HouseDetailRepository houseDetailRepository;
	@Autowired
	private HousePictureRepository housePictureRepository;
	@Autowired
	private HouseTagRepository houseTagRepository;
	@Value("${alioss.cdn.prefix}")
	private String cdnPrefix;

	@Override
	public void saveHousePic(String aliOss) {

		// houseRepository.save(entity);
	}

	@Override
	@Transactional
	public ServiceResult<HouseDTO> save(HouseForm houseForm) {
		HouseDetail detail = new HouseDetail();
		ServiceResult<HouseDTO> subwayValidtionResult = wrapperDetilInfo(detail, houseForm);
		if (subwayValidtionResult != null) {
			return subwayValidtionResult;
		}

		House house = new House();
		modelMapper.map(houseForm, house);

		Date now = new Date();
		house.setCreateTime(now);
		house.setLastUpdateTime(now);
		house.setAdminId(LoginUserUtil.getLoginUserId());
		// 添加房屋
		house = houseRepository.save(house);

		detail.setHouseId(house.getId());

		// 添加房屋细节信息
		detail = houseDetailRepository.save(detail);
		List<HousePicture> pictures = generatePictures(houseForm, house.getId());
		// 添加房屋图片
		Iterable<HousePicture> housePictures = housePictureRepository.save(pictures);
		List<String> tags = houseForm.getTags();
		if (tags != null && !tags.isEmpty()) {
			List<HouseTag> houseTags = new ArrayList<>();
			for (String tag : tags) {
				houseTags.add(new HouseTag(house.getId(), tag));
			}
			// 添加房屋标签
			houseTagRepository.save(houseTags);
		}
		// 封装housedto
		HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
		HouseDetailDTO houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);
		houseDTO.setHouseDetail(houseDetailDTO);
		List<HousePictureDTO> pictureDTOS = new ArrayList<>();
		housePictures.forEach(housePicture -> pictureDTOS.add(modelMapper.map(housePicture, HousePictureDTO.class)));
		houseDTO.setPictures(pictureDTOS);
		// 添加封面
		houseDTO.setCover(cdnPrefix + houseDTO.getCover());
		return new ServiceResult<HouseDTO>(true, null, houseDTO);
	}

	public ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody) {
		List<HouseDTO> houseDTOs = new ArrayList<>();
		// 分页的升序还是降序 分页的字段名称
		Sort sort = new Sort(Sort.Direction.fromString(searchBody.getDirection()), searchBody.getOrderBy());
		int page = searchBody.getStart() / searchBody.getLength();
		Pageable pageable = new PageRequest(page, searchBody.getLength(), sort);

		Specification<House> specification = new Specification<House>() {

			@Override
			public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.equal(root.get("adminId"), LoginUserUtil.getLoginUserId());
				predicate = cb.and(predicate, cb.notEqual(root.get("status"), HouseStatus.DELETED.getValue()));

				if (searchBody.getCity() != null) {
					predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), searchBody.getCity()));
				}

				if (searchBody.getStatus() != null) {
					predicate = cb.and(predicate, cb.equal(root.get("status"), searchBody.getStatus()));
				}

				if (searchBody.getCreateTimeMin() != null) {
					predicate = cb.and(predicate,
							cb.greaterThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMin()));
				}

				if (searchBody.getCreateTimeMax() != null) {
					predicate = cb.and(predicate,
							cb.lessThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMax()));
				}

				if (searchBody.getTitle() != null) {
					predicate = cb.and(predicate, cb.like(root.get("title"), "%" + searchBody.getTitle() + "%"));
				}

				return predicate;
			}
		};
		Page<House> houses = houseRepository.findAll(pageable);

		houses.forEach(house -> {
			HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
			houseDTO.setCover(house.getCover());
			houseDTOs.add(houseDTO);
		});
		return new ServiceMultiResult<>(houses.getTotalElements(), houseDTOs);
	}

	@Override
	public ServiceResult<HouseDTO> findHouseOne(Long id) {
		// 房屋信息
		House house = houseRepository.findOne(id);
		if (house == null) {
			return ServiceResult.notFound();
		}
		// 房屋细节
		HouseDetail detail = houseDetailRepository.findByHouseId(id);
		List<HousePicture> pictures = housePictureRepository.findAllByHouseId(id);
		HouseDetailDTO detailDTO = modelMapper.map(detail, HouseDetailDTO.class);
		List<HousePictureDTO> pictureDTOS = new ArrayList<>();
		for (HousePicture picture : pictures) {
			HousePictureDTO pictureDTO = modelMapper.map(picture, HousePictureDTO.class);
			pictureDTOS.add(pictureDTO);
		}

		List<HouseTag> tags = houseTagRepository.findAllByHouseId(id);
		List<String> tagList = new ArrayList<>();
		for (HouseTag tag : tags) {
			tagList.add(tag.getName());
		}
		HouseDTO result = modelMapper.map(house, HouseDTO.class);
		result.setHouseDetail(detailDTO);
		result.setPictures(pictureDTOS);
		result.setTags(tagList);
		return ServiceResult.of(result);
	}

	@Override
	@Transactional
	public ServiceResult update(HouseForm houseForm) {
		House house = houseRepository.findOne(houseForm.getId());
		if (house == null) {
			return ServiceResult.notFound();
		}
		HouseDetail detail = houseDetailRepository.findByHouseId(houseForm.getId());
		if (detail == null) {
			return ServiceResult.notFound();
		}
		ServiceResult<HouseDTO> wrapperResult = wrapperDetilInfo(detail, houseForm);
		if (wrapperResult != null) {
			return wrapperResult;
		}
		houseDetailRepository.save(detail);
		List<HousePicture> pictures = generatePictures(houseForm, houseForm.getId());
		housePictureRepository.save(pictures);
		if (houseForm.getCover() == null) {
			houseForm.setCover(house.getCover());
		}
		modelMapper.map(houseForm, house);
		house.setLastUpdateTime(new Date());
		houseRepository.save(house);
		
		if(house.getStatus() == HouseStatus.PASSES.getValue()){
			searchService.index(house.getId());
		}else{
			searchService.remove(house.getId());
		}
		 
		return ServiceResult.success();
	}

	/**
	 * 图片对象列表信息填充
	 * 
	 * @param form
	 * @param houseId
	 * @return
	 */
	private List<HousePicture> generatePictures(HouseForm houseform, Long houseId) {
		List<HousePicture> pictures = new ArrayList<>();
		if (houseform.getPhotos() == null || houseform.getPhotos().isEmpty()) {
			return pictures;
		}
		for (PhotoForm photoForm : houseform.getPhotos()) {
			HousePicture housePicture = new HousePicture();
			housePicture.setHouseId(houseId);
			housePicture.setCdnPrefix(cdnPrefix);
			housePicture.setPath(photoForm.getPath());
			housePicture.setWidth(photoForm.getWidth());
			housePicture.setHeight(photoForm.getHeight());
			pictures.add(housePicture);
		}
		return pictures;
	}

	/**
	 * 房屋细节对象列表信息填充
	 * 
	 * @param detail
	 * @param houseForm
	 * @return
	 */
	private ServiceResult<HouseDTO> wrapperDetilInfo(HouseDetail detail, HouseForm houseForm) {
		Subway subway = subwayRepository.findOne(houseForm.getSubwayLineId());
		if (subway == null) {
			return new ServiceResult<>(false, "Not valid subway line");
		}
		SubwayStation subwayStation = subwayStationRepository.findOne(houseForm.getSubwayStationId());
		if (subwayStation == null) {
			return new ServiceResult<>(false, "Not valid subway station");
		}

		detail.setSubwayLineId(subway.getId());
		detail.setSubwayLineName(subway.getName());
		detail.setSubwayStationId(subwayStation.getId());
		detail.setSubwayStationName(subwayStation.getName());
		detail.setDescription(houseForm.getDescription());
		detail.setDetailAddress(houseForm.getDetailAddress());
		detail.setLayoutDesc(houseForm.getLayoutDesc());
		detail.setRentWay(houseForm.getRentWay());
		detail.setRoundService(houseForm.getRoundService());
		detail.setTraffic(houseForm.getTraffic());
		return null;
	}

	@Override
	@Transactional
	public ServiceResult removePhoto(Long id) {
		HousePicture picture = housePictureRepository.findOne(id);
		if (picture == null) {
			return ServiceResult.notFound();
		}
		return null;
	}

	@Override
	@Transactional
	public ServiceResult removeTag(Long houseId, String tag) {
		House house = houseRepository.findOne(houseId);
		if (house == null) {
			return ServiceResult.notFound();
		}
		HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
		if (houseTag == null) {
			return new ServiceResult(false, "标签不存在");
		}
		houseTagRepository.delete(houseTag.getId());
		return ServiceResult.success();
	}

	@Override
	@Transactional
	public ServiceResult addTag(Long houseId, String tag) {
		House house = houseRepository.findOne(houseId);
		if (house == null) {
			return ServiceResult.notFound();
		}
		HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
		if (houseTag != null) {
			return new ServiceResult(false, "标签已存在");
		}
		houseTagRepository.save(new HouseTag(houseId, tag));
		return ServiceResult.success();
	}

	@Override
	@Transactional
	public ServiceResult updateCover(Long coverId, Long targetId) {
		HousePicture cover = housePictureRepository.findOne(coverId);
		if (cover == null) {
			return ServiceResult.notFound();
		}
		houseRepository.updateCover(targetId, cover.getPath());
		return ServiceResult.success();
	}

	@Override
	@Transactional
	public ServiceResult updateStatus(Long id, int status) {
		House house = houseRepository.findOne(id);
		if (house == null) {
			return ServiceResult.notFound();
		}
		if (house.getStatus() == status) {
			return new ServiceResult(false, "状态没有发生变化");
		}
		if (house.getStatus() == HouseStatus.RENTED.getValue()) {
			return new ServiceResult(false, "已出租的房源不允许修改状态");
		}
		if (house.getStatus() == HouseStatus.DELETED.getValue()) {
			return new ServiceResult(false, "已删除的资源不允许操作");
		}

		houseRepository.updateStatus(id, status);
		if(status == HouseStatus.PASSES.getValue()){
			searchService.index(id);
		}else{
			searchService.remove(id);
			
		}
		return ServiceResult.success();
	}

	@Override
	public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {
		// 排序
		// Sort sort = new Sort(Sort.Direction.DESC,"lastUpdateTime");
		Sort sort = HouseSort.generateSort(rentSearch.getOrderBy(), rentSearch.getOrderDirection());
		// 分页
		int page = rentSearch.getStart() / rentSearch.getSize();
		Pageable pageable = new PageRequest(page, rentSearch.getSize(), sort);
		List<HouseDTO> houseDTOs = new ArrayList<>();
		// 分装查询维度条件
		Specification<House> sprcification = new Specification<House>() {

			@Override
			public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.equal(root.get("status"), HouseStatus.PASSES.getValue());
				predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), rentSearch.getCityEnName()));
				return predicate;
			}
		};

		Page<House> houses = houseRepository.findAll(sprcification, pageable);
		houses.forEach(house -> {
			houseDTOs.add(modelMapper.map(house, HouseDTO.class));
			
		});
		return new ServiceMultiResult<>(houses.getTotalElements(), houseDTOs);
	}

	@Override
	public ServiceResult<HouseDTO> findCompleteOne(Long houseId) {
		House house = houseRepository.findOne(houseId);
		HouseDetail houseDetail = houseDetailRepository.findByHouseId(houseId);
		List<HouseTag> housetags = houseTagRepository.findAllByHouseId(houseId);
		List<HousePicture> housePictures = housePictureRepository.findAllByHouseId(houseId);
		List<String> tagList = new ArrayList<>();
		List<HousePictureDTO> pictureDTOS = new ArrayList<>();
		housetags.forEach(housetag -> tagList.add(housetag.getName()));
		housePictures.forEach(housePicture -> pictureDTOS.add(modelMapper.map(housePicture, HousePictureDTO.class)));
		if (house == null) {
            return ServiceResult.notFound();
        }
		
		HouseDetailDTO houseDetailDTO = modelMapper.map(houseDetail, HouseDetailDTO.class);
		
		HouseDTO result = modelMapper.map(house, HouseDTO.class);
		result.setHouseDetail(houseDetailDTO);
		result.setTags(tagList);
		result.setPictures(pictureDTOS);
		return ServiceResult.of(result);
	}

}
