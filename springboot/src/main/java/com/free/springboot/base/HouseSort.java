package com.free.springboot.base;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.Sets;


public class HouseSort {
	
	//默认分类
	public static final String DEFAULT_SORT_KEY = "lastUpdateTime";

	//到地铁站的距离
	public static final String DISTANCE_TO_SUBWAY_KEY = "distanceToSubway";
	
	//排序集
	private static final Set<String> SORT_KEYS = Sets.newHashSet(
			DEFAULT_SORT_KEY,
			"createTime",
			"price",
			"area",
			DISTANCE_TO_SUBWAY_KEY
			);
	
	/**
	 * 生成排序
	 * @return
	 */
	public static Sort generateSort(String key ,String directionKey){
        if (!SORT_KEYS.contains(key)) {
            key = DEFAULT_SORT_KEY;
        }
        Direction direction = Sort.Direction.fromStringOrNull(directionKey);
        if (direction == null) {
            direction = Sort.Direction.DESC;
        }
		return new Sort(direction ,key);
	}
	
}
