package com.free.springboot.service.serch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.free.springboot.base.HouseSort;
import com.free.springboot.base.RentValueBlock;
import com.free.springboot.dto.BaiduMapLocation;
import com.free.springboot.dto.HouseBucketDTO;
import com.free.springboot.dto.HouseDTO;
import com.free.springboot.entity.House;
import com.free.springboot.entity.HouseDetail;
import com.free.springboot.entity.HouseTag;
import com.free.springboot.entity.SupportAddress;
import com.free.springboot.form.RentSearch;
import com.free.springboot.repository.HouseDetailRepository;
import com.free.springboot.repository.HouseRepository;
import com.free.springboot.repository.HouseTagRepository;
import com.free.springboot.repository.SupportAddressRepository;
import com.free.springboot.service.AddressService;
import com.free.springboot.service.ServiceMultiResult;
import com.free.springboot.service.ServiceResult;

@Service
public class SearchServiceImpl implements SearchService {

	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String INDEX_NAME = "xunwu";
	private static final String INDEX_TYPE = "house";
	private static final String INDEX_TOPIC = "house_build";
	@Autowired
	private HouseRepository houseRepository;
	@Autowired
	private HouseTagRepository houseTagRepository;
	@Autowired
	private HouseDetailRepository houseDetailRepository;
	@Autowired
    private SupportAddressRepository supportAddressRepository;
	@Autowired
    private AddressService addressService;
	@Autowired
	private TransportClient esClient;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@KafkaListener(topics = INDEX_TOPIC)
	private void handleMessage(String content){
		try {
			HouseIndexMessage indexMessage = MAPPER.readValue(content, HouseIndexMessage.class);
			 switch (indexMessage.getOperation()) {
	             case HouseIndexMessage.INDEX:
	                 this.createOrUpdateIndex(indexMessage);
	                 System.out.println("index");
	                 break;
	             case HouseIndexMessage.REMOVE:
	                 this.removeIndex(indexMessage);
	                 break;
	             default:
	                 logger.warn("Not support message content " + content);
	                 break;
	         }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void index(Long houseId) {
		this.index(houseId ,0);
	}

	@Override
	public void remove(Long houseId) {
		this.remove(houseId ,0);
	}
	
	@Override
	public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.filter(
				QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, rentSearch.getCityEnName())
		);
		if(rentSearch.getRegionEnName() != null && ("*").equals(rentSearch.getRegionEnName())){
			boolQuery.filter(
					QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, rentSearch.getRegionEnName())
			);
		}
		RentValueBlock area = RentValueBlock.matchArea(rentSearch.getAreaBlock());
		if(!RentValueBlock.ALL.equals(area)){
			RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(HouseIndexKey.AREA);
			if(area.getMax() > 0){
				rangeQuery.lte(area.getMax());
			}
			if(area.getMin() > 0){
				rangeQuery.gte(area.getMin());
			}
			boolQuery.filter(rangeQuery);
		}
		RentValueBlock price = RentValueBlock.matchPrice(rentSearch.getPriceBlock());
		if(!RentValueBlock.ALL.equals(price)){
			RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(HouseIndexKey.PRICE);
			if(area.getMax() > 0){
				rangeQuery.lte(price.getMax());
			}
			if(area.getMin() > 0){
				rangeQuery.gte(price.getMin());
			}
			boolQuery.filter(rangeQuery);
		}
		if(rentSearch.getDirection() > 0){
			boolQuery.filter(
					QueryBuilders.termQuery(HouseIndexKey.DIRECTION, rentSearch.getDirection())
			);
		}
		if(rentSearch.getRentWay() > -1){
			boolQuery.filter(
					QueryBuilders.termQuery(HouseIndexKey.RENT_WAY, rentSearch.getRentWay())
			);
		}
		
		boolQuery.must(
                QueryBuilders.multiMatchQuery(rentSearch.getKeywords(),
                        HouseIndexKey.TITLE,
                        HouseIndexKey.TRAFFIC,
                        HouseIndexKey.DISTRICT,
                        HouseIndexKey.ROUND_SERVICE,
                        HouseIndexKey.SUBWAY_LINE_NAME,
                        HouseIndexKey.SUBWAY_STATION_NAME
                ));
		boolQuery.must(
				QueryBuilders.multiMatchQuery(rentSearch.getKeywords(), HouseIndexKey.TITLE,
                        HouseIndexKey.TRAFFIC,
                        HouseIndexKey.DISTRICT,
                        HouseIndexKey.ROUND_SERVICE,
                        HouseIndexKey.SUBWAY_LINE_NAME,
                        HouseIndexKey.SUBWAY_STATION_NAME)
				);
		
		SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
			.setTypes(INDEX_TYPE)
			.setQuery(boolQuery)
			.addSort(
				HouseSort.getSortKey(rentSearch.getOrderBy()),
				SortOrder.fromString(rentSearch.getOrderDirection())
			 )
			.setFrom(rentSearch.getStart())
			.setSize(rentSearch.getSize())
			.setFetchSource(HouseIndexKey.HOUSE_ID, null);
		 logger.debug(requestBuilder.toString());
		return null;
	}
	
	private void removeIndex(HouseIndexMessage indexMessage) {
		Long houseId = indexMessage.getHouseId();
		DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
			.newRequestBuilder(esClient)
			.filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId))
			.source(INDEX_NAME);
		logger.debug("Delete by query for house: " + builder);
		BulkByScrollResponse response = builder.get();
		long deleted = response.getDeleted();
		logger.debug("Delete total " + deleted);
		
		if (deleted <= 0) {
			this.remove(indexMessage.getHouseId(), indexMessage.getRetry() + 1);
		} else {
			logger.debug("Did not remove data from es for response: " + response);
		}
	}

	private void createOrUpdateIndex(HouseIndexMessage indexMessage) {
		Long houseId = indexMessage.getHouseId();
		House house = houseRepository.findOne(houseId);
		HouseIndexTemplate indexTemplate = new HouseIndexTemplate();
		HouseDetail houseDetail = houseDetailRepository.findByHouseId(houseId);
		if (house == null || houseDetail == null) {
			logger.error("没有此房源信息Index house {} dose not exist!", houseId);
			this.index(houseId ,indexMessage.getRetry() + 1);
			return;
		}
		List<HouseTag> houseTags = houseTagRepository.findAllByHouseId(houseId);
		SupportAddress city = supportAddressRepository.findByEnNameAndLevel(house.getCityEnName(), SupportAddress.Level.CITY.getValue());
		SupportAddress region = supportAddressRepository.findByEnNameAndLevel(house.getRegionEnName(), SupportAddress.Level.REGION.getValue());
		modelMapper.map(house, indexTemplate);
		modelMapper.map(houseDetail, indexTemplate);
		if (houseTags != null && houseTags.size() != 0) {
			List<String> tags = new ArrayList<>();
			houseTags.forEach(houseTag -> tags.add(houseTag.getName()));
			indexTemplate.setTags(tags);
		}
		String address = city.getCnName()+region.getEnName()+ house.getStreet() + house.getDistrict() + houseDetail.getDetailAddress();
		ServiceResult<BaiduMapLocation> location = addressService.getBaiduMapLocation(city.getCnName(), address);
		if (!location.isSuccess()) {
            this.index(indexMessage.getHouseId(), indexMessage.getRetry() + 1);
            return;
        }
		indexTemplate.setLocation(location.getResult());
		logger.debug("ceshi");
		SearchRequestBuilder builder = esClient.prepareSearch(INDEX_NAME)
				.setTypes(INDEX_TYPE).setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId));
		logger.debug(builder.toString());
		SearchResponse searchResponse = builder.get();
		long totalHits = builder.get().getHits().getTotalHits();
		
		boolean success = false;
		System.out.println(totalHits);
		if(totalHits == 0){
			// create
			success = create(indexTemplate);
		}else if(totalHits == 1){
			// update
			success = update(builder.get().getHits().getAt(0).getId() ,indexTemplate);
		}else{
			// deleteAndCreate
			success = deleteAndCreate(totalHits ,indexTemplate);
		}
		
		if (!success) {
			this.index(indexMessage.getHouseId(), indexMessage.getRetry() + 1);
		} else {
			logger.debug("Index success with house " + houseId);
		}
	}
	
	/**
	 * remove index 消息队列
	 * @param houseId
	 * @param retry
	 */
	private void index(Long houseId ,int retry){
		if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("Retry index times over 3 for house: " + houseId + " Please check it!");
            return;
        }
		HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.INDEX, retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC, MAPPER.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("Json encode error for " + message);
        }
	}
	private void remove(Long houseId, int retry) {
		if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("Retry index times over 3 for house: " + houseId + " Please check it!");
            return;
        }
		HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.REMOVE, retry);
		try {
			this.kafkaTemplate.send(INDEX_TOPIC, MAPPER.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			logger.error("Cannot encode json for " + message, e);
		}
	}

	private boolean create(HouseIndexTemplate indexTemplate) {
		try {
			IndexResponse response = esClient.prepareIndex(INDEX_NAME, INDEX_TYPE)
					.setSource(MAPPER.writeValueAsBytes(indexTemplate), XContentType.JSON).get();
			logger.debug("create index with house" + indexTemplate.getHouseId());
			if (response.status() == RestStatus.CREATED) {
				return true;
			}
			return false;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean update(String esId, HouseIndexTemplate indexTemplate) {
		try {
			UpdateResponse response = esClient.prepareUpdate(INDEX_NAME, INDEX_TYPE, esId)
					.setDoc(MAPPER.writeValueAsBytes(indexTemplate), XContentType.JSON).get();
			logger.debug("create index with house" + indexTemplate.getHouseId());
			if (response.status() == RestStatus.OK) {
				return true;
			}
			return false;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean deleteAndCreate(long totalHit ,HouseIndexTemplate indexTemplate) {
		DeleteByQueryRequestBuilder deleteBuilder = DeleteByQueryAction
				.INSTANCE.newRequestBuilder(esClient)
				.filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, indexTemplate.getHouseId()))
				.source(INDEX_NAME);
		logger.debug("Delete by query for house: " + deleteBuilder);
		BulkByScrollResponse response = deleteBuilder.get();
		long deleted = response.getDeleted();
		if(deleted != totalHit){
			logger.warn("Need delete {}, but {} was deleted!", totalHit, deleted);
			return false;
		}
		return create(indexTemplate);
	}

	@Override
	public ServiceResult<List<String>> suggest(String prefix) {
		return null;
	}

	@Override
	public ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName) {
		//创建boolQueryBuilder查询
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		//过滤参数 根据城市过滤
		boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName));
		//根据区域聚合
		TermsAggregationBuilder aggregation = AggregationBuilders
				.terms(HouseIndexKey.AGG_REGION)
				.field(HouseIndexKey.REGION_EN_NAME);
		
		SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
			.setTypes(INDEX_TYPE)
			.setQuery(boolQuery)
			.addAggregation(aggregation);
		SearchResponse response = requestBuilder.get();
		List<HouseBucketDTO> buckets = new ArrayList<>();
		if(response.status() != RestStatus.OK){
			logger.warn("Aggregete status is not ok for "+ requestBuilder);
			return new ServiceMultiResult<>(0, buckets);
		}
		logger.debug(requestBuilder.toString());
		Terms terms = response.getAggregations().get(HouseIndexKey.AGG_REGION);
		for (Terms.Bucket bucket : terms.getBuckets()) {
			buckets.add(new HouseBucketDTO(bucket.getKeyAsString(), bucket.getDocCount()));
		}
		return new ServiceMultiResult<HouseBucketDTO>(response.getHits().getTotalHits(), buckets);
	}

}
