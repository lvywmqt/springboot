package com.cgltech.cat_conn.util;

import com.alibaba.fastjson.JSONObject;

/* 
后台返回前台结果类。
实例举例：
[{	
	"code":200,
	"data":{"column":"666"},
	"message":"success"
}]
初始化的默认值：
		ActionResult ar = new ActionResult();
		JSONObject jsonObject = JSONObject.fromObject(ar); 
		System.out.println(jsonObject);//{"code":0,"data":null,"message":""}
*/
public class CatPacketData {
	
	public static final int CODE_SUCCESS = 200;

	private static final int CODE_DEFAULT = 500;
	private static final int CODE_BUSY = 202;
	private static final int CODE_TIMEOUT = 203;
	private static final int CODE_OFF_LINE = 204;

	public static CatPacketData newSuccess(Object data) {
		
		CatPacketData catPacketData = new CatPacketData(CODE_SUCCESS, "success!");
		catPacketData.data = data;
		return catPacketData;
	}
	
	public static CatPacketData newFailed(String message) {
		CatPacketData catPacketData = new CatPacketData(CODE_DEFAULT, message);
		return catPacketData;
	}
	
	public static CatPacketData newFailed(String message, JSONObject response) {
		CatPacketData catPacketData = new CatPacketData(CODE_DEFAULT, message);

		response.put("data", null);
		catPacketData.data = response;
		return catPacketData;
	}
	
	public static CatPacketData newFailedBusy(JSONObject response) {
		CatPacketData catPacketData = new CatPacketData(CODE_BUSY, "client busy!");

		response.put("data", null);
		catPacketData.data = response;
		return catPacketData;
	}
	
	public static CatPacketData newFailedTimeout(JSONObject response) {
		CatPacketData catPacketData = new CatPacketData(CODE_TIMEOUT, "client timeout!");
		response.put("data", null);
		catPacketData.data = response;
		return catPacketData;
	}

	public static CatPacketData newFailedOffline(JSONObject response) {
		
		CatPacketData catPacketData = new CatPacketData(CODE_OFF_LINE, "off line!");

		response.put("data", null);
		catPacketData.data = response;
		
		return catPacketData;
	}
	
	private int code;//200：成功，400：请求错误，
	private String message;
	private Object data;
	
	public CatPacketData() {
		
	}
	
	public CatPacketData(int code) {
		this.code = code;
	}
	
	public CatPacketData(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
}
