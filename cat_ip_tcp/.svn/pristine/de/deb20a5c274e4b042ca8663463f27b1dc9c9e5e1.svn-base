package com.cgltech.cat_ip_tcp.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;

//下面一行定义java bean中的属性被序列化后的顺序（为了与硬件猫的软件兼容）。
//因为发送给硬件猫的数据，需要严格按照约定的顺序格式传输，所以每次发送前都转化为此Java bean，以确保顺序无误。
//硬件猫发送给服务器的数据，json顺序没有特别要求。
@JSONType(orders={"data","deviceNo","method"})
public class TransmissionParameterVO {

	private String method;
//	private String version;
	private String deviceNo;
//	private String message;
//	private String code;
//	private String time;
	private JSONObject data;
	private String time;
//	private Integer value;
	
	
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
//	public String getVersion() {
//		return version;
//	}
//	public void setVersion(String version) {
//		this.version = version;
//	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
//	public String getCode() {
//		return code;
//	}
//	public void setCode(String code) {
//		this.code = code;
//	}
//	public String getTime() {
//		return time;
//	}
//	public void setTime(String time) {
//		this.time = time;
//	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	
}
