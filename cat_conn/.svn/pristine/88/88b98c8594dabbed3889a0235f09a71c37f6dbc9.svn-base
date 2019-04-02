package com.cgltech.cat_conn.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.util.DateUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;


public class CatUploadDeviceStatusHandler extends CatClientHandler{

	private static Logger logger = LoggerFactory.getLogger(CatUploadDeviceStatusHandler.class);
		
	private static String currDateTime = DateUtil.getSdfTimes();
	private static AtomicInteger initialTerminalNum = new AtomicInteger();
	private static String REQUEST_FLAG = "TEST";
	public static AttributeKey<String> deviceNoKey = AttributeKey.valueOf("deviceNokey");
	
	public static void main(String[] args){
		
		if (args != null && args.length > 0) {
			REQUEST_FLAG = args[0];
		}

		CatClient.init(new CatUploadDeviceStatusHandler());
		int catNum = 3000;
		for (int i = 0; i < catNum; i++) {
			new Thread(() -> {
				CatClient.connect();
			}).start();
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	} 
	
	// 连接成功后，向server发送消息  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
		//{"data":null,"deviceNo":"201803250001","method":"uploadDeviceStatus", "time":"" }
    	
    	StringBuilder deviceNo = new StringBuilder();
    	deviceNo.append(REQUEST_FLAG);
    	deviceNo.append("-");
    	deviceNo.append(currDateTime);
    	deviceNo.append("-");
    	deviceNo.append(initialTerminalNum.incrementAndGet());
    	
    	Attribute<String> deviceNoAttribute = ctx.channel().attr(deviceNoKey);
    	deviceNoAttribute.compareAndSet(null, deviceNo.toString());
    	
    	TransmissionParameterVO transmissionParameterVO = new TransmissionParameterVO();
    	transmissionParameterVO.setDeviceNo(deviceNo.toString());
    	transmissionParameterVO.setMethod("uploadDeviceStatus");
    	transmissionParameterVO.setTime("");
    	transmissionParameterVO.setData(null);
    	
    	String requestJson = JSONObject.toJSONString(transmissionParameterVO, SerializerFeature.WriteMapNullValue);
    	
        logger.info("[deviceNo:{}]: send data is：[{}]", deviceNo.toString(), requestJson);

    	requestJson += "\n";

    	ctx.channel().writeAndFlush(requestJson);
 
    }

}
