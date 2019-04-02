/**
 * 
* 类名称： HandleForCutTicketCatBiz.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月16日
* @version 2.0
 */
package com.cgltech.cat_conn.server.cat.handle.impl;

import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.server.cat.handle.IHandleCatServer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author User
 *
 */
public class HandleForReadyReceiveInstruction implements IHandleCatServer {

	private static final Logger logger = LoggerFactory.getLogger(HandleForReadyReceiveInstruction.class);
	
	@Override
	public String handle(ChannelHandlerContext context, TransmissionParameterVO transmissionParameterVO) {

		/*
		{"data":{"received":"true"},
			"deviceNo":"201803250001","method":"ready_receive_instruction", "time":""} 
		*/

		TransmissionParameterVO response = new TransmissionParameterVO();
    	response.setDeviceNo(transmissionParameterVO.getDeviceNo());
    	response.setMethod("ready_receive_instruction");
    	response.setTime(String.valueOf(Clock.systemDefaultZone().millis()));
    	JSONObject data = new JSONObject(true);
    	data.put("received", "true");
    	
    	response.setData(data);
		
    	logger.info("[deviceNo:{}]:{}", transmissionParameterVO.getDeviceNo(), JSONObject.toJSONString(response));
    	return null;
	}
}
