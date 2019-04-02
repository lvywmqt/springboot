package com.cgltech.cat_conn.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

import io.netty.channel.ChannelHandlerContext;

public class CatDeviceInitHandler extends CatClientHandler{

	private static Logger logger = LoggerFactory.getLogger(CatDeviceInitHandler.class);
	public static void main(String[] args){
		CatClient.init(new CatDeviceInitHandler());
		CatClient.connect();
	}
	
	// 连接成功后，向server发送消息  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
    	/*{
			"data":{"fwVersion":"xx1","hwVersion":"xxx","serialNo":"yyy","devtype":"TCM01M1" },
			"deviceNo":"201803250001","method":"deviceInit", "time":"123456789"}
    	*/
    	TransmissionParameterVO transmissionParameterVO = new TransmissionParameterVO();
    	transmissionParameterVO.setDeviceNo(TCommonParam.TERMINAL_CODE);
    	transmissionParameterVO.setMethod("deviceInit");
    	transmissionParameterVO.setTime("");
    	
    	Data data = new Data();
    	data.setDevtype("TCM01M1");
    	data.setFwVersion("xx1");
    	data.setHwVersion("xxx");
    	data.setSerialNo("yyy");
    	
    	transmissionParameterVO.setData((JSONObject)JSONObject.toJSON(data));
    	
    	String requestJson = JSONObject.toJSONString(transmissionParameterVO, SerializerFeature.WriteMapNullValue);
    	requestJson += "\n";
        logger.info("Client send data is：{}", requestJson);
        
    	ctx.channel().writeAndFlush(requestJson);
 
    }
    
    static class Data{
    	
    	private String fwVersion;
    	private String hwVersion;
    	private String serialNo;
    	private String devtype;
		public String getFwVersion() {
			return fwVersion;
		}
		public void setFwVersion(String fwVersion) {
			this.fwVersion = fwVersion;
		}
		public String getHwVersion() {
			return hwVersion;
		}
		public void setHwVersion(String hwVersion) {
			this.hwVersion = hwVersion;
		}
		public String getSerialNo() {
			return serialNo;
		}
		public void setSerialNo(String serialNo) {
			this.serialNo = serialNo;
		}
		public String getDevtype() {
			return devtype;
		}
		public void setDevtype(String devtype) {
			this.devtype = devtype;
		}
    	
    }
}
