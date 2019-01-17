package com.free.springboot.service.serch;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.free.springboot.entity.House;
import com.free.springboot.entity.HouseDetail;
import com.free.springboot.entity.HouseTag;
import com.free.springboot.repository.HouseDetailRepository;
import com.free.springboot.repository.HouseRepository;
import com.free.springboot.repository.HouseTagRepository;

import jdk.nashorn.internal.runtime.Source;

@Service
public class SearchServiceImpl implements SearchService {

	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String INDEX_NAME = "xunwu";
	private static final String INDEX_TYPE = "house";
	@Autowired
	private HouseRepository houseRepository;
	@Autowired
	private HouseTagRepository houseTagRepository;
	@Autowired
	private HouseDetailRepository houseDetailRepository;
	@Autowired
	private TransportClient esClient;
	@Autowired
	private ModelMapper mapper;

	@Override
	public void index(Long houseId) {
		House house = houseRepository.findOne(houseId);
		HouseIndexTemplate indexTemplate = new HouseIndexTemplate();
		HouseDetail houseDetail = houseDetailRepository.findByHouseId(houseId);
		List<HouseTag> houseTags = houseTagRepository.findAllByHouseId(houseId);
		if (house == null || houseDetail == null) {
			logger.error("没有此房源信息Index house {} dose not exist!", houseId);
			return;
		}
		mapper.map(house, indexTemplate);
		mapper.map(houseDetail, indexTemplate);
		if (houseTags != null && houseTags.size() != 0) {
			List<String> tags = new ArrayList<>();
			houseTags.forEach(houseTag -> tags.add(houseTag.getName()));
			indexTemplate.setTags(tags);
		}
		SearchRequestBuilder builder = esClient.prepareSearch(INDEX_NAME)
				.setTypes(INDEX_TYPE).setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId));
		logger.debug(builder.toString());
		SearchResponse searchResponse = builder.get();
		long totalHits = builder.get().getHits().getTotalHits();
		System.out.println(totalHits);
		
		boolean success = false;
		if(totalHits == 0){
			System.out.println("tianjia");
			// create
			success = create(indexTemplate);
		}else if(totalHits == 1){
			// update
			success = update(builder.get().getHits().getAt(0).getId() ,indexTemplate);
		}else{
			// deleteAndCreate
			success = deleteAndCreate(totalHits ,indexTemplate);
		}
	}

	@Override
	public void remove(Long houseId) {
		DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
		.newRequestBuilder(esClient)
		.filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId))
		.source(INDEX_NAME);
		
		logger.debug("delete by query house" + houseId);
		BulkByScrollResponse response = builder.get();
		long deleted = response.getDeleted();
		logger.debug("delete total" + deleted);
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

}
