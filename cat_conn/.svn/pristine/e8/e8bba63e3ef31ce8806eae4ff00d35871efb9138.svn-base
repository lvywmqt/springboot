package com.cgltech.cat_conn.server.cat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgltech.cat_conn.util.DateUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public enum CatServerManager {
 
	INSTANCE;
 
	private Logger logger = LoggerFactory.getLogger(CatServerManager.class);
    
	/** 缓存通信上下文环境对应的登录用户（主要用于服务） */ 
	private final Map<CatIoSession, String> session2DeviceNos  = new ConcurrentHashMap<>();
 
	/** 缓存用户id与对应的会话 */
	private final Map<String, CatIoSession> deviceNo2Sessions = new ConcurrentHashMap<>();
 
	public Map<String, CatIoSession> getAllDevices(){
		return deviceNo2Sessions;
	}
	
	public int getSessionTotal(){
		return deviceNo2Sessions.size();
	}
	
	public boolean isOnline(String deviceNo){
		return deviceNo2Sessions.containsKey(deviceNo);
	}
	
	/**
	 *  向所有在线用户发送数据包
	 */
	public void sendDataToAllDevices(String data){
		if(data == null ) return;
 
		deviceNo2Sessions.values().forEach( (session) -> session.sendData(data));
	}
 
	/**
	 *  服务器主动-向单一设备发送数据包
	 */
	public boolean sendDataTo(String data, Channel channel){
		
		if(data == null || channel == null) return false;
		
		CatIoSession catIoSession = getSessionBy(channel);

		boolean lock = lock(catIoSession);
        
    	if(lock){
    		catIoSession.sendData(data);
    	}
    	return lock;
	}
	/**
	 *  服务器主动-向单一设备发送数据包
	 */
	public boolean sendDataTo(String data,String deviceNo, Consumer<ChannelFuture> consumer){
		if(data == null || deviceNo == null) return false;
 
		CatIoSession catIoSession = deviceNo2Sessions.get(deviceNo);
		boolean lock = lock(catIoSession);

    	if(lock){
    		catIoSession.sendData(data, consumer);
    	}
		return lock;
	}

	/**
	 * 服务器响应-向设备发送数据包，不锁定设备
	 * @param data
	 * @param channel
	 */
	public void responseDataTo(String data, Channel channel){
		
		if(data == null || channel == null) return;
		
		CatIoSession session = getSessionBy(channel);
		if (session != null) {
			session.sendData(data);
		}
	}
	
	/**
	 * 注册session(绑定设备和channel)
	 * @param deviceNo
	 * @param channel
	 * @return
	 */
	public boolean registerSession(String deviceNo, Channel channel) {
 
		CatIoSession oldSession = this.deviceNo2Sessions.get(deviceNo);
		unRegisterSession(oldSession, SessionCloseReason.RE_REGISTER);
		
		CatIoSession session = getSessionBy(channel);
		session.setAllowSendData(true);
		
    	//获取yyyy-MM-dd HH:mm:ss.SSS格式的时间
    	String connectTime = DateUtil.getCurrentTimeMillis_h();
    	session.setConnectTime(connectTime);
		session.setDeviceNo(deviceNo);
		
		deviceNo2Sessions.put(deviceNo, session);
		session2DeviceNos.put(session, deviceNo);

		logger.info("[deviceNo:{}]: registered...", deviceNo);
 
		return true;
	}
 
	/**
	 *   注销用户通信渠道
	 */
	public void unRegisterSession(Channel context, SessionCloseReason sessionCloseReason){
		if(context == null){
			return;
		}
		CatIoSession session = getSessionBy(context);
		
		unRegisterSession(session, sessionCloseReason);
	}

	private void unRegisterSession(CatIoSession session, SessionCloseReason sessionCloseReason){

		if (session == null) {
			return;
		}
		String deviceNo = session2DeviceNos.remove(session);
		
		if(deviceNo != null){
			deviceNo2Sessions.remove(deviceNo);
		}

		if (sessionCloseReason == null) {
			sessionCloseReason = SessionCloseReason.NORMAL;
		}

		session.close(sessionCloseReason);

		logger.info("[deviceNo:{}]: un registered...", deviceNo);
	}

	public CatIoSession getSessionBy(Channel channel) {
		
		CatIoSession session = ChannelUtils.getSessionBy(channel);

		return session;
	}
	
	public String getDeviceNoBy(Channel channel){
		
		CatIoSession catIoSession = getSessionBy(channel);
		String deviceNo = null;
    	
    	if (catIoSession != null && catIoSession.getDeviceNo() != null) {
    		deviceNo = catIoSession.getDeviceNo();
		}
    	
    	return deviceNo;
	}
    
    /**
     * 给设备解锁，允许发送指令
     * @param channel
     * @return
     */
    public boolean unlock(Channel channel) {
    	boolean unlocked = false;
		CatIoSession catIoSession = getSessionBy(channel);
		unlocked = unlock(catIoSession);
    	return unlocked;
    }
    
    /**
     * 给设备解锁，允许给设备发送指令
     * @param deviceNo
     * @return
     */
    public boolean unlock(String deviceNo) {
    	boolean unlocked = false;
		CatIoSession catIoSession = deviceNo2Sessions.get(deviceNo);
    	unlocked = unlock(catIoSession);
    	return unlocked;
    }
    
    /**
     * 给设备加锁，防止给设备发送多条指令
     * @param deviceNo
     * @return
     */
    private boolean lock(CatIoSession catIoSession) {
    	boolean locked = false;
    	if (catIoSession != null && catIoSession.getDeviceNo() != null) {
	    	synchronized (catIoSession) {
	    		if (catIoSession.isAllowSendData()) {
	    			catIoSession.setAllowSendData(false);
	            	locked = true;
				}
			}
    	}
		logger.info("[deviceNo:{}]: lock [{}]", catIoSession.getDeviceNo(), locked);
    	return locked;
    }
    
    private boolean unlock(CatIoSession catIoSession) {
    	boolean unlocked = false;
    	if (catIoSession != null) {
    		synchronized (catIoSession) {
        		if (!catIoSession.isAllowSendData()) {
        			catIoSession.setAllowSendData(true);
                	unlocked = true;
    			}
    		}
		}
		logger.info("[deviceNo:{}]: unlock [{}]", catIoSession.getDeviceNo(), unlocked);
    	return unlocked;
    }

}