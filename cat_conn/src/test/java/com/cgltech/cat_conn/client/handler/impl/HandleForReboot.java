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
public class HandleForReboot implements IHandleCatClient {

	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {
		/*
		{"data":null,"deviceNo":"201803250001","method":"return_reboot", "time":"123456789"}
		*/
		TransmissionParameterVO response = new TransmissionParameterVO();
		
		response.setDeviceNo(transmissionParameterVO.getDeviceNo());
		response.setMethod("return_reboot");
		response.setTime(transmissionParameterVO.getTime());
		
		JSONObject data = new JSONObject(true);

		response.setData(data);
		
		return JSONObject.toJSONString(response, 
				SerializerFeature.WriteMapNullValue);
	}

}
