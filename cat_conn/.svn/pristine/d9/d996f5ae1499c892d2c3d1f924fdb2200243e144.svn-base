package com.cgltech.cat_conn.server.cat;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class CatIoSession {
	
	private static final Logger logger = LoggerFactory.getLogger(CatIoSession.class);

	private static final String PACKET_SPLIT_CHART = "\n";
	
	/** 网络连接channel */
	private Channel channel;
 
	private String deviceNo;
 
	/** ip地址 */
	private String ipAddr;
 	
	/** 猫的状态（能否发送数据） */
	private boolean allowSendData;
	/** 开始建立长连接的时间 */
	private String connectTime;
	
	private int reConnectionCount;
	
	/** 拓展用，保存一些个人数据  */
	private Map<String, Object> attrs = new HashMap<>();
 
	public CatIoSession() {

	}
 
	/**
	 * 向客户端发送消息
	 * @param data
	 */
	public void sendData(String data) {
		sendData(data, null);
	}
	public void sendData(String data, Consumer<ChannelFuture> consumer) {
		if (data == null) {
			return;
		}
		if (channel != null) {
			String packetData = appendPacketSplit(data);

			channel.writeAndFlush(packetData).addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						logger.info("[deviceNo:{}]: server send data [{}] is success.", deviceNo, data);
					} else {
	                    CatServerManager.INSTANCE.unlock(deviceNo);
						logger.info("[deviceNo:{}]: server send data [{}] is failed.", deviceNo, data);
					}
					if (consumer != null) {
						consumer.accept(future);
					}					
				}
			});
		}
	}
	
	private String appendPacketSplit(String data){
		if (!data.endsWith(PACKET_SPLIT_CHART)) {
			data += PACKET_SPLIT_CHART;
		}
		return data;
	}
	
	public boolean isClose() {
		if (channel == null) {
			return true;
		}
		return !channel.isActive() ||
			   !channel.isOpen();
	}
	
	/**
	 * 关闭session 
	 * @param reason {@link SessionCloseReason}
	 */
	public void close(SessionCloseReason reason) {
		try{
			if (this.channel == null) {
				return;
			}
			if (channel.isOpen()) {
				ChannelUtils.delChannelSession(channel, this);
				channel.close();
				logger.info("[deviceNo:{}] close session, reason is {}", deviceNo, reason);
			}else{
				logger.info("[deviceNo:{}] already close, reason is {}", deviceNo, reason);
			}
		}catch(Exception e){
		}
	}
	
	public CatIoSession(Channel channel) {
		this.channel = channel;
		this.ipAddr = ChannelUtils.getIp(channel);
	}
	
	public String getIpAddr() {
		return ipAddr;
	}
 
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
 
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public boolean isAllowSendData() {
		return allowSendData;
	}

	public void setAllowSendData(boolean allowSendData) {
		this.allowSendData = allowSendData;
	}

	public String getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(String connectTime) {
		this.connectTime = connectTime;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getReConnectionCount() {
		return reConnectionCount;
	}

	public void setReConnectionCount(int reConnectionCount) {
		this.reConnectionCount = reConnectionCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CatIoSession [channel=");
		builder.append(channel);
		builder.append(", deviceNo=");
		builder.append(deviceNo);
		builder.append(", ipAddr=");
		builder.append(ipAddr);
		builder.append(", allowSendData=");
		builder.append(allowSendData);
		builder.append(", connectTime=");
		builder.append(connectTime);
		builder.append(", reConnectionCount=");
		builder.append(reConnectionCount);
		builder.append(", attrs=");
		builder.append(attrs);
		builder.append("]");
		return builder.toString();
	}

	
}