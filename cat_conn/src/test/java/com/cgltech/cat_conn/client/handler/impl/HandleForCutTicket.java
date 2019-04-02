/**
 * 
* 类名称： HandleForCutTicket.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月16日
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
public class HandleForCutTicket implements IHandleCatClient {

	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {
		/*
			{
		        "method": "return_cutTicket", 
		        "data": {"requestSaleNum":"6","realSaleNum":"3","errcode":"5002","orderId": "20983432432432"}, 
		        "deviceNo": "xxx"
		    }
		*/
		TransmissionParameterVO response = new TransmissionParameterVO();
		
		response.setDeviceNo(transmissionParameterVO.getDeviceNo());
		response.setMethod("return_cutTicket");
		response.setTime(transmissionParameterVO.getTime());
		
		JSONObject data = new JSONObject(true);
    	data.put("requestSaleNum", "6");
    	data.put("realSaleNum", "3");
    	data.put("orderNo", "20983432432432");
    	data.put("errcode", "5002");
		response.setData(data);
		
		return JSONObject.toJSONString(response, 
				SerializerFeature.WriteMapNullValue);
	}

}
