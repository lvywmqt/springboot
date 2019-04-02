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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.listener.ApplicationContextFactory;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.server.cat.future.SyncWrite;
import com.cgltech.cat_conn.server.cat.handle.IHandleCatServer;
import com.cgltech.cat_conn.service.TicketCutDetailsService;
import com.cgltech.cat_conn.service.dto.UpdateTicketCutResultDto;
import com.cgltech.cat_conn.service.impl.TicketCutDetailsServiceImpl;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author User
 *
 */
public class HandleForCutTicket implements IHandleCatServer {

	private static final Logger logger = LoggerFactory.getLogger(HandleForCutTicket.class);
	
	@Override
	public String handle(ChannelHandlerContext context, TransmissionParameterVO transmissionParameterVO) {
		
		//time不为空时同步返回出票结果
		if (transmissionParameterVO.getTime() != null
				&& !transmissionParameterVO.getTime().equals("")) {
			
			String jsonString = JSONObject.toJSONString(
	    			transmissionParameterVO, SerializerFeature.WriteMapNullValue);
			
			//匹配请求
	        String requestId = transmissionParameterVO.getDeviceNo() + "_" 
	        				+ transmissionParameterVO.getTime();

	        SyncWrite.syncResponse(requestId, jsonString);
	        
			return null;
		}else{//time为空时通过http接口返回出票结果
			
	    	JSONObject data = new JSONObject(true);
	    	data.put("orderNo", transmissionParameterVO.getData().get("orderNo"));

			try {
				TicketCutDetailsService ticketCutDetailsService = ApplicationContextFactory.INSTANCE.getService(TicketCutDetailsServiceImpl.class);
				
				UpdateTicketCutResultDto updateTicketCutResultDto = new UpdateTicketCutResultDto();

				JSONObject requestdata = transmissionParameterVO.getData();
				
				updateTicketCutResultDto.setErrcode(requestdata.getString("errcode"));
				updateTicketCutResultDto.setOrderNo(requestdata.getString("orderNo"));
				updateTicketCutResultDto.setSuccessSaleNum(requestdata.getInteger("realSaleNum"));
				updateTicketCutResultDto.setTimestamp(transmissionParameterVO.getTime());
				updateTicketCutResultDto.setRequestSaleNum(requestdata.getInteger("requestSaleNum"));
				
				ticketCutDetailsService.updateTicketCutResult(updateTicketCutResultDto);
				
				data.put("flag", "true");
				
			} catch (Exception e) {
				logger.error("[deviceNo:{}]:更新出票结果错误[{}]", transmissionParameterVO.getDeviceNo(), transmissionParameterVO, e);
				data.put("flag", "false");
			}
			
			TransmissionParameterVO response = new TransmissionParameterVO();
	    	response.setDeviceNo(transmissionParameterVO.getDeviceNo());
	    	response.setMethod("roger_return_cutTicket");
	    	response.setTime(String.valueOf(Clock.systemDefaultZone().millis()));
			response.setData(data);
			
			return JSONObject.toJSONString(response);
		}
	}
}
