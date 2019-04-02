/**
 * 
* 类名称： HandleForGetStatus.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月16日
* @version 2.0
 */
package com.cgltech.cat_conn.server.cat.handle.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.server.cat.future.SyncWrite;
import com.cgltech.cat_conn.server.cat.handle.IHandleCatServer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author User
 *
 */
public class HandleForGetStatus implements IHandleCatServer {

	@Override
	public String handle(ChannelHandlerContext context, TransmissionParameterVO transmissionParameterVO) {
		/** 
		 *
		 "data": {
			"ticketLength": "1524",
			"time_receive": "2018-04-15 20:09:11.333"
		},
		"deviceNo": "201803250001",
		"method": "return_getTicketLength",
		"time": "2018-04-15 20:09:01.333"
		 **/
		
		String jsonString = JSONObject.toJSONString(
    			transmissionParameterVO, SerializerFeature.WriteMapNullValue);
		
		//匹配请求
        String requestId = transmissionParameterVO.getDeviceNo() + "_" 
        				+ transmissionParameterVO.getTime();

		SyncWrite.syncResponse(requestId, jsonString);
		return null;
	}

}
