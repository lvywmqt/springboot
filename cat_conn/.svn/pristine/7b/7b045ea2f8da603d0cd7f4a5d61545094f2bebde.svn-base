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
public class HandleForGetGprsStatus implements IHandleCatClient {

	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {
		/*
		"data": {
			"ccid": "xx",
			"ip": "xx",
			"signalQuality": "cc",
			"serverIP": "xx.xx.xx.xx",
			"time_receive": "2018-04-15 20:09:11.333"
		},
		"deviceNo": "201803250001",
		"method": "return_getGprsStatus",
		"time": "2018-04-15 20:09:01.333"
		*/
		TransmissionParameterVO response = new TransmissionParameterVO();
		
		response.setDeviceNo(transmissionParameterVO.getDeviceNo());
		response.setMethod("return_getGprsStatus");
		response.setTime(transmissionParameterVO.getTime());
		
		JSONObject data = new JSONObject(true);
		data.put("ccid", "xx");
		data.put("ip", "xx");
		data.put("signalQuality", "yyy");
		data.put("serverIP", "xx");
		data.put("time_receive", "2018-04-15 20:09:11.333");

		response.setData(data);
		
		return JSONObject.toJSONString(response, 
				SerializerFeature.WriteMapNullValue);
	}

}
