package com.cgltech.cat_conn.server.cat.future;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.server.cat.CatServerManager;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.util.CatPacketData;
import com.cgltech.cat_conn.util.DateUtil;

public class SyncWrite<T> {

	private static Logger LOGGER = LoggerFactory.getLogger(SyncWrite.class);

	public static Map<String, WriteFuture<String>> syncWriteFutures = new ConcurrentHashMap<>();

	public static Map<String, PriorityBlockingQueue<GetStatusTask>> allGetStatusTasks = new ConcurrentHashMap<>();

	public static CatPacketData writeAndSync(
			final TransmissionParameterVO tParameterVO, 
			final long timeout,
			JSONObject responseJson){

		if (timeout <= 0) {
			throw new IllegalArgumentException("timeout <= 0");
		}

		CatPacketData catPacketData = null;
		if (!CatServerManager.INSTANCE.isOnline(tParameterVO.getDeviceNo())) {
			catPacketData = CatPacketData.newFailedOffline(responseJson);
		} else {

			WriteFuture<String> writeFuture = new SyncWriteFuture<>();
			// 将【设备编号】+【_】+【时间戳】作为请求id，标识不同的请求。
			String requestId = tParameterVO.getDeviceNo() + "_" + tParameterVO.getTime();
	
			syncWriteFutures.put(requestId, writeFuture);
			
			try {
				LOGGER.info("[deviceNo:{}]: 发送请求，请求参数[{}]", tParameterVO.getDeviceNo(), tParameterVO);

				String data = JSONObject.toJSONString(tParameterVO, SerializerFeature.WriteMapNullValue);
				
				if("getStatus".equals(tParameterVO.getMethod())){
					
					GetStatusTask getStatusTask = new GetStatusTask();
					getStatusTask.setTransmissionParameterVO(tParameterVO);
					getStatusTask.setTimeout(timeout);
					getStatusTask.setResponseJson(responseJson);
					getStatusTask.setData(data);
					getStatusTask.setWriteFuture(writeFuture);
					
					new Thread(() -> {
						getStatus(requestId, getStatusTask);
					}).start();
					
					catPacketData = response(getStatusTask.getWriteFuture(), 
							getStatusTask.getTimeout(), 
							getStatusTask.getTransmissionParameterVO(), 
							getStatusTask.getResponseJson());
					
				}else{
					boolean result = CatServerManager.INSTANCE.sendDataTo(data, 
							tParameterVO.getDeviceNo(),
							(future) -> {
								if (writeFuture != null) {
									writeFuture.setWriteResult(future.isSuccess());
									writeFuture.setCause(future.cause());
								}
							});

					if (result) {
						catPacketData = response(writeFuture, timeout, tParameterVO, responseJson);
					}else{
						catPacketData = CatPacketData.newFailedBusy(responseJson);
					}
				}				
			} finally {
				syncWriteFutures.remove(requestId);
			}
		}
		return catPacketData;
	}

	public static void syncResponse(String requestId, String data) {

		WriteFuture<String> future = syncWriteFutures.get(requestId);
		if (future != null) {
			future.setResponse(data);
		} else {
			LOGGER.error("[requestId:{}]: 同步请求响应错误，syncWriteFuture为空，响应数据：[{}]", requestId, data);
		}
	}
	
	private static CatPacketData response(final WriteFuture<String> writeFuture, 
			final long timeout, final TransmissionParameterVO tParameterVO,
			JSONObject responseJson){
		
		CatPacketData catPacketData = null;
		String response = null;
		try {
			response = (String) writeFuture.get(timeout, TimeUnit.MILLISECONDS);
			
			if (response == null) {
				LOGGER.error("[deviceNo:{}]: 请求失败，请求参数[{}]", tParameterVO.getDeviceNo(), tParameterVO, writeFuture.getCause());
				catPacketData = CatPacketData.newFailed("给设备发送数据错误", responseJson);
			}else{
				responseJson = JSONObject.parseObject(response);

				((JSONObject) responseJson.get("data")).put("time_receive", DateUtil.getCurrentTimeMillis_h());
				String Request_time = (String) responseJson.get("time");
				responseJson.put("time",
						(String) DateUtil.sdfCurrentTimeMillis_h.format(new Date(Long.parseLong(Request_time))));

				catPacketData = CatPacketData.newSuccess(responseJson);
			}
		} catch (Exception e) {
			LOGGER.error("[deviceNo:{}]: 请求失败，请求参数[{}]", tParameterVO.getDeviceNo(), tParameterVO, e);
			catPacketData = CatPacketData.newFailed("给设备发送数据错误", responseJson);
		}
		LOGGER.info("[deviceNo:{}]: 请求结果[{}]", tParameterVO.getDeviceNo(), response);

		return catPacketData;
	}
	
	private static void getStatus(String requestId, GetStatusTask getStatusTask){
		
		PriorityBlockingQueue<GetStatusTask> getStatusTasks;
		if(allGetStatusTasks.containsKey(getStatusTask.getTransmissionParameterVO().getDeviceNo())){
			getStatusTasks = allGetStatusTasks.get(getStatusTask.getTransmissionParameterVO().getDeviceNo());
		}else{
			getStatusTasks = new PriorityBlockingQueue<GetStatusTask>(100);
			allGetStatusTasks.put(getStatusTask.getTransmissionParameterVO().getDeviceNo(), getStatusTasks);	
		}

		boolean result = CatServerManager.INSTANCE.sendDataTo(getStatusTask.getData(), 
				getStatusTask.getTransmissionParameterVO().getDeviceNo(),
				(future) -> {
					if (getStatusTask.getWriteFuture() != null) {
						getStatusTask.getWriteFuture().setWriteResult(future.isSuccess());
						getStatusTask.getWriteFuture().setCause(future.cause());
					}
				});
		
		if (!result && syncWriteFutures.containsKey(requestId)){
			getStatusTasks.add(getStatusTask);

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					getStatus(requestId, getStatusTasks.poll());
				}
			}, 200);
		}
	}
	
	static class GetStatusTask implements Comparable<GetStatusTask>{
		
		private TransmissionParameterVO transmissionParameterVO;
		private long timeout;
		private JSONObject responseJson;
		private String data; 
		private WriteFuture<String> writeFuture;

		@Override
		public int compareTo(GetStatusTask task) {
			return this.transmissionParameterVO.getTime().compareTo(
					task.getTransmissionParameterVO().getTime());
		}
		
		public long getTimeout() {
			return timeout;
		}
		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}
		public JSONObject getResponseJson() {
			return responseJson;
		}
		public void setResponseJson(JSONObject responseJson) {
			this.responseJson = responseJson;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public WriteFuture<String> getWriteFuture() {
			return writeFuture;
		}
		public void setWriteFuture(WriteFuture<String> writeFuture) {
			this.writeFuture = writeFuture;
		}
		public TransmissionParameterVO getTransmissionParameterVO() {
			return transmissionParameterVO;
		}
		public void setTransmissionParameterVO(TransmissionParameterVO transmissionParameterVO) {
			this.transmissionParameterVO = transmissionParameterVO;
		}
	}
}
