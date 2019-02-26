package com.free.springboot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.free.springboot.service.serch.SearchService;

public class SearchTest extends SpringbootApplicationTests {
	@Autowired
	private SearchService searchService;

	@Test
	public void testIndex() {
		Long targetHouseId = 15L;
		System.out.println(1);
		searchService.index(targetHouseId);
		System.out.println(2);
	}
	
	@Test
	public void testRemove() {
		Long targetHouseId = 17L;
		System.out.println(1);
		searchService.remove(targetHouseId);
		System.out.println(2);
	}
}
