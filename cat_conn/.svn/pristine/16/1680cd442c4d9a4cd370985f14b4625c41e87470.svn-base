/**
 * 
* 类名称： HandleForUploadDeviceStatus.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月16日
* @version 2.0
 */
package com.cgltech.cat_conn.server.cat.handle.impl;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cgltech.cat_conn.server.cat.CatServerManager;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.server.cat.handle.IHandleCatServer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author User
 *
 */
public class HandleForUploadDeviceStatus implements IHandleCatServer{

	@Override
	public String handle(ChannelHandlerContext context, TransmissionParameterVO transmissionParameterVO) {
		
		//第一次请求成功,注册session会话
		CatServerManager.INSTANCE.registerSession(transmissionParameterVO.getDeviceNo(), 
				context.channel());
	    
	    Map<String, String> value = new HashMap<>();
	    value.put("status", "0");
    	
    	TransmissionParameterVO response = new TransmissionParameterVO();

    	JSONObject data = new JSONObject(true);
    	data.put("flag", true);
    	
    	response.setData(data);
    	response.setDeviceNo(transmissionParameterVO.getDeviceNo());
    	response.setMethod("return_uploadDeviceStatus");
    	response.setTime(String.valueOf(Clock.systemDefaultZone().millis()));
		
		return JSONObject.toJSONString(response);
	}

}
