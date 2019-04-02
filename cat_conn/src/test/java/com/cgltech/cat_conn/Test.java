package com.cgltech.cat_conn;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.util.JsonUtil;
import com.cgltech.cat_conn.util.ToolUtil;
import com.google.gson.Gson;

public class Test {

	public static void df(){
		String data = ToolUtil.readTxtFile("G:/readtxt/txt.txt");
		
		TransmissionParameterVO tp = null;
		tp = new Gson().fromJson(data, TransmissionParameterVO.class);
		
		
		String jsonString = JSONObject.toJSONString(tp);
		System.out.println(jsonString);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		String jsonString_data = jsonObject.getString("data");
		
		
		Map<String,String> map=new ConcurrentHashMap<String, String>();
		map  = JsonUtil.convertJsonStrToMap(jsonString_data);
		System.out.println(map);
	}

	
	
	public static void test1(){
		String json = "";
		JSONObject jsonObj = new JSONObject();  
		Map m= jsonObj.parseObject(json, LinkedHashMap.class); 
		
		
		
	}
	public static void test2(){
		
		TransmissionParameterVO t = new  TransmissionParameterVO();
		t.setMethod("methiod");
		t.setDeviceNo("dddd");
//		t.setData(null);
		String jsonString = JSONObject.toJSONString(t,SerializerFeature.WriteMapNullValue);
		System.out.println(jsonString);

		
	}
	public static void test3(){
		String data = ToolUtil.readTxtFile("G:/readtxt/txt1.txt");
//		JSONObject jsonObject = JSONObject.parseObject(data);
//		String jsonString_data = jsonObject.getString("data");
//		LinkedHashMap<String, String> jsonMap = JSON.parseObject(data, new TypeReference<LinkedHashMap<String, String>>() {});
		LinkedHashMap<String, Object> jsonMap = JSON.parseObject(data,LinkedHashMap.class,Feature.OrderedField);
//		String jsonString_data = jsonMap.get("data");
//		数据为==> {"data":{"ccid":"xx","ip":"xx","signalQuality":"cc"},"deviceNo":"56435345","method":"getGprsStatus"}
//		Exception in thread "main" java.lang.ClassCastException: com.alibaba.fastjson.JSONObject cannot be cast to java.lang.String
//			at com.cgltech.lottery_conn.Test.test3(Test.java:93)
//			at com.cgltech.lottery_conn.Test.main(Test.java:20)
		
		JSONObject jsonObject = (JSONObject)jsonMap.get("data");
		// 第一种方式
	    String jsonString = JSONObject.toJSONString(jsonObject);
		System.out.println(jsonString);
//		System.out.println(jsonString_data);
//	    //第二种方式,使用Gson思想
//		TransmissionParameter tt = JSONObject.parseObject(data, TransmissionParameter.class);
//	    System.out.println(tt);
//		
//		Map<String,String> map=new ConcurrentHashMap<String, String>();
//		map  = JsonUtil.convertJsonStrToMap(jsonString_data);
//		System.out.println(map);
	}
	public static void testOrder(){
		JSONObject jsonObject = new JSONObject(false);

		jsonObject.put("ccc", "333");
		jsonObject.put("aaa", "111");
		jsonObject.put("bbb", "222");

		System.out.println(jsonObject.toString());	//{"ccc":"333","aaa":"111","bbb":"222"}
		System.out.println(jsonObject.toJSONString());//{"ccc":"333","aaa":"111","bbb":"222"}
		String order =  JSONObject.toJSONString(jsonObject, SerializerFeature.SortField);
		System.out.println(order);

		
	}
	public static void testOrder3(){
		String data =  ToolUtil.readTxtFile("G:/readtxt/txt1.txt");
		TransmissionParameterVO student = JSONObject.parseObject(data, TransmissionParameterVO.class);
		System.out.println((student.getData())instanceof JSONObject );
		System.out.println(student.getData()==null);
		System.out.println(((JSONObject) student.getData()).get("ccid").toString());
		
	}
	public static void main(String[] args){
//		test2();
		
		 Map<String,String> map = new HashMap<String,String>();
    	 map.put("flag", "");
    	 System.out.println(map.get("ddd"));
		
	}
}
