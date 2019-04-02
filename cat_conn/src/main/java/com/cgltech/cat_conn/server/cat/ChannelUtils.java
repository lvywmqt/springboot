package com.cgltech.cat_conn.server.cat;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public final class ChannelUtils {
	
	public static AttributeKey<CatIoSession> SESSION_KEY = AttributeKey.valueOf("session");
	
	/**
	 * 添加新的会话
	 * @param channel
	 * @param session
	 * @return
	 */
	public static boolean addChannelSession(Channel channel, CatIoSession session) {
		Attribute<CatIoSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.compareAndSet(null, session);
	}
	
	public static boolean delChannelSession(Channel channel, CatIoSession session) {
		Attribute<CatIoSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.compareAndSet(session, null);
	}
	
	public static CatIoSession getSessionBy(Channel channel) {
		Attribute<CatIoSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.get() ;
	}
	
	public static String getIp(Channel channel) {
		return ((InetSocketAddress)channel.remoteAddress()).getAddress().toString().substring(1);
	}
 
}