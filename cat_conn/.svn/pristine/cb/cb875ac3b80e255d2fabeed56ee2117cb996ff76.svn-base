package com.cgltech.cat_conn.server.cat;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;

//下面一行定义java bean中的属性被序列化后的顺序（为了与硬件猫的软件兼容）。
//因为发送给硬件猫的数据，需要严格按照约定的顺序格式传输，所以每次发送前都转化为此Java bean，以确保顺序无误。
//硬件猫发送给服务器的数据，json顺序没有特别要求。
@JSONType(orders={"data","deviceNo","method"})
public class TransmissionParameterVO {

	private String method;
	private String deviceNo;
	private JSONObject data;
	private String time;
	
	
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
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransmissionParameterVO [method=");
		builder.append(method);
		builder.append(", deviceNo=");
		builder.append(deviceNo);
		builder.append(", data=");
		builder.append(data);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}
}
