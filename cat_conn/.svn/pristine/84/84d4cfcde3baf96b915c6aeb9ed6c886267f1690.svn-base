/**
 * 
* 类名称： HandleForGetTicketLength.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月17日
* @version 2.0
 */
package com.cgltech.cat_conn.client.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.client.handler.IHandleCatClient;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

/**
 * @author User
 *
 */
public class HandleForGetStatus implements IHandleCatClient {

	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {
		/*
		"data": {
	 		"errorcode":1002",
			"flag": "true",
			"time_receive": "2018-04-15 20:09:11.333"
		 },
		"deviceNo": "201803250001",
		"method": "getStatus",
		"time": "2018-04-15 20:09:01.333"
			{"data":{"flag":"true","errorcode":1002"},"deviceNo":"201803250001","method":"return_getStatus", "time":"123456789"}

		*/
		TransmissionParameterVO response = new TransmissionParameterVO();
		
		response.setDeviceNo(transmissionParameterVO.getDeviceNo());
		response.setMethod("return_getStatus");
		response.setTime(transmissionParameterVO.getTime());
		
		JSONObject data = new JSONObject(true);
		data.put("errorcode", "1002");
		data.put("flag", "true");
		data.put("time_receive", "2018-04-15 20:09:11.333");

		response.setData(data);
		
		return JSONObject.toJSONString(response, 
				SerializerFeature.WriteMapNullValue);
	}

}
