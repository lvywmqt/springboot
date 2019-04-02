package com.cgltech.cat_conn.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

import io.netty.channel.ChannelHandlerContext;

public class CatUploadCutTicketResultHandler extends CatClientHandler{

	private static Logger logger = LoggerFactory.getLogger(CatUploadCutTicketResultHandler.class);
	public static void main(String[] args){
		CatClient.init(new CatUploadCutTicketResultHandler());
		CatClient.connect();
	}
	
	// 连接成功后，向server发送消息  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
		//{"data":{"requestSaleNum":"6","realSaleNum":"3","errorcode":"1002","orderNo":"A3101040000120180413140043197"},"deviceNo":"201803250001", "method":"uploadCutTicketResult", "time":""}

    	TransmissionParameterVO transmissionParameterVO = new TransmissionParameterVO();
    	transmissionParameterVO.setDeviceNo(TCommonParam.TERMINAL_CODE);
    	transmissionParameterVO.setMethod("uploadCutTicketResult");
    	transmissionParameterVO.setTime("");
    	
    	Data data = new Data();
    	data.setErrorcode("1002");
    	data.setOrderNo("A3101040000120180413140043197");
    	data.setRealSaleNum("6");
    	data.setRequestSaleNum("3");
    	
    	transmissionParameterVO.setData((JSONObject)JSONObject.toJSON(data));
    	
    	String requestJson = JSONObject.toJSONString(transmissionParameterVO, SerializerFeature.WriteMapNullValue);
    	requestJson += "\n";
        logger.info("Client send data is：{}", requestJson);
        
    	ctx.channel().writeAndFlush(requestJson);
 
    }
    
    static class Data{
    	private String requestSaleNum;
    	private String realSaleNum;
    	private String errorcode;
    	private String orderNo;
		public String getRequestSaleNum() {
			return requestSaleNum;
		}
		public void setRequestSaleNum(String requestSaleNum) {
			this.requestSaleNum = requestSaleNum;
		}
		public String getRealSaleNum() {
			return realSaleNum;
		}
		public void setRealSaleNum(String realSaleNum) {
			this.realSaleNum = realSaleNum;
		}
		public String getErrorcode() {
			return errorcode;
		}
		public void setErrorcode(String errorcode) {
			this.errorcode = errorcode;
		}
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
    	
    	
    }
}
