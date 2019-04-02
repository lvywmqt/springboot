package com.cgltech.cat_conn.client;

import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

import io.netty.channel.ChannelHandlerContext;

public class CatReadyReceiveInstructionHandler extends CatClientHandler{

	private static Logger logger = LoggerFactory.getLogger(CatReadyReceiveInstructionHandler.class);
	public static void main(String[] args){
		CatClient.init(new CatReadyReceiveInstructionHandler());
		CatClient.connect();
	}
	
	// 
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
		//{"data":{"ready":"true"},"deviceNo":"201803250001", "method":"ready_receive_instruction", "time":""}

    	TransmissionParameterVO transmissionParameterVO = new TransmissionParameterVO();
    	transmissionParameterVO.setDeviceNo(TCommonParam.TERMINAL_CODE);
    	transmissionParameterVO.setMethod("ready_receive_instruction");
    	transmissionParameterVO.setTime(String.valueOf(Clock.systemDefaultZone().millis()));
    	
    	Data data = new Data();
    	data.setReady("true");
    	
    	transmissionParameterVO.setData((JSONObject)JSONObject.toJSON(data));
    	
    	String requestJson = JSONObject.toJSONString(transmissionParameterVO, SerializerFeature.WriteMapNullValue);
    	requestJson += "\n";
        logger.info("Client send data is：{}", requestJson);
        
    	ctx.channel().writeAndFlush(requestJson);
 
    }
    
    static class Data{
    	private String ready;

		public String getReady() {
			return ready;
		}

		public void setReady(String ready) {
			this.ready = ready;
		}
    	
    }
}
