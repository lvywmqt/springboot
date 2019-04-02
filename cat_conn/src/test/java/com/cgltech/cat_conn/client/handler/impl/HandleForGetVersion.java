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
public class HandleForGetVersion implements IHandleCatClient {

	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {
		/*
		"data": {
			"fwVersion": "xx",
			"hwVersion": "xx",
			"serialNo": "yyy",
			"devtype": "xx",
			"time_receive": "2018-04-15 20:09:11.333"
		},
		"deviceNo": "201803250001",
		"method": "return_getVersion",
		"time": "2018-04-15 20:09:01.333"
		*/
		TransmissionParameterVO response = new TransmissionParameterVO();
		
		response.setDeviceNo(transmissionParameterVO.getDeviceNo());
		response.setMethod("return_getVersion");
		response.setTime(transmissionParameterVO.getTime());
		
		JSONObject data = new JSONObject(true);
		data.put("fwVersion", "xx");
		data.put("hwVersion", "xx");
		data.put("serialNo", "yyy");
		data.put("devtype", "xx");
		
		data.put("time_receive", "2018-04-15 20:09:11.333");

		response.setData(data);
		
		return JSONObject.toJSONString(response, 
				SerializerFeature.WriteMapNullValue);
	}

}
