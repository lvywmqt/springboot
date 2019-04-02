package com.cgltech.cat_ip_tcp.util;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class NettyHandlerUtil {
	
	//添加回车，然后发送
	public static ChannelFuture appendTagAndWriteAndFlush(Channel channel, String data){
		data+="\n";
		return channel.writeAndFlush(data);
	}
	

}
