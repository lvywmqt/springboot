package com.cgltech.cat_conn;

import java.util.HashMap;
import java.util.Map;

import com.cgltech.cat_conn.util.JedisUtils;
import com.cgltech.cat_conn.util.MyLog;

public class TestRedis {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String test = JedisUtils.get("key");
//		Map<String, String> value = new HashMap<String,String>();
////		MyLog.info(test+"ttt");
//		value.put("key1", "rr");
//		value.put("key2", "rr");
////		value.put("key3", "3");
////		value.put("key4", "4");
////		String key = "hashkey";
//		
//		JedisUtils.mapPut("key",value);
////		MyLog.info("getMap");
////		MyLog.info(JedisUtils.getMap("key").toString());
//		JedisUtils.getMap("key").toString();
		JedisUtils.del("key");
//		mapPut
	}

}
