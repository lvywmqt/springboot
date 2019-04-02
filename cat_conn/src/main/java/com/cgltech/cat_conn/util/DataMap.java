package com.cgltech.cat_conn.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataMap {
	public static Map<String, ConcurrentHashMap<String, String>> map = 
			new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();

	   
}
