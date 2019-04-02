package com.cgltech.cat_conn.server.cat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cgltech.cat_conn.server.cat.handle.CatServerHandlerFactory;
import com.cgltech.cat_conn.server.cat.handle.IHandleCatServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;


public class CatServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory
            .getLogger(CatServerHandler.class);
    
    private static final String HEARTBEAT_PING = "1";
    private static final String HEARTBEAT_PONG = "2";
    
    private static final String HEARTBEAT_PING_SERVER = "a";
    private static final String HEARTBEAT_PONG_SERVER = "b";
    
    
    protected String name;

    public CatServerHandler(String name) {
        this.name = name;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {

    	String deviceNo = CatServerManager.INSTANCE.getDeviceNoBy(context.channel());
    	String data = msg.toString();
//    	long beginTime = System.currentTimeMillis();
    	
    	if (HEARTBEAT_PING.equals(data)) {//兼容旧版本客户端主动心跳
    		
    		logger.info("[deviceNo:{}]: server receive data :[{}].", deviceNo, data);
    		CatServerManager.INSTANCE.responseDataTo(HEARTBEAT_PONG, context.channel());
    		
        }else if (HEARTBEAT_PONG_SERVER.equals(data)) {//新版本服务端主动心跳

			logger.info("[deviceNo:{}]: server receive data :[{}].", deviceNo, data);
			CatServerManager.INSTANCE.unlock(deviceNo);

		}else{

			TransmissionParameterVO tp = null;
			try {
				tp = JSONObject.parseObject(data, TransmissionParameterVO.class);
			}catch (Exception e) {				
				logger.error("[deviceNo:{}]: server parse data [{}] error.", deviceNo, data, e);

	            CatServerManager.INSTANCE.unlock(context.channel());
			}
			
			if (tp != null) {
				//第一次连接时deviceNo重新设置为请求参数中的deviceNo
				if (deviceNo == null) {
					deviceNo = tp.getDeviceNo();
				}
				logger.info("[deviceNo:{}]: server receive data :[{}].", deviceNo, data);

	        	hanlde(context, tp, deviceNo);
			}
        }
//    	logger.info("[deviceNo:{}]: server handle data [{}] run time:[{}]毫秒.", deviceNo, msg, 
//    			System.currentTimeMillis() - beginTime);
    }

    private void hanlde(ChannelHandlerContext context, TransmissionParameterVO tp, String deviceNo){
    	try {
    		String methodName = tp.getMethod();

            IHandleCatServer handleCatServer = CatServerHandlerFactory.getCatHandler(methodName);

            String response = handleCatServer.handle(context, tp);
            
            //response不为空给客户端发送指令
            if (!StringUtils.isEmpty(response)) {
                CatServerManager.INSTANCE.responseDataTo(response, context.channel());
            }else{
            	CatServerManager.INSTANCE.unlock(context.channel());
            }
		}catch (Exception e) {		
			logger.error("[deviceNo:{}]: server handle data [{}] error.", deviceNo, tp, e);

            CatServerManager.INSTANCE.unlock(context.channel());
		}
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:{
                	CatServerManager.INSTANCE.unRegisterSession(ctx.channel(), 
                			SessionCloseReason.READ_OVER_TIME);
                    break;
                }
                case WRITER_IDLE:{
                    break;
                }
                case ALL_IDLE:{
                	CatServerManager.INSTANCE.sendDataTo(HEARTBEAT_PING_SERVER, ctx.channel());
                    break;
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	
    	logger.info("{} is active.", ctx.channel().remoteAddress());
    	
    	if (!ChannelUtils.addChannelSession(ctx.channel(), new CatIoSession(ctx.channel()))) {
			ctx.channel().close();
			logger.error("Duplicate session,IP=[{}].", ChannelUtils.getIp(ctx.channel()));
		}
    	
    }

    //连接断开时调用
    @Override
    public void channelInactive(ChannelHandlerContext context) {
    	
        CatServerManager.INSTANCE.unRegisterSession(context.channel(), SessionCloseReason.DISCONNECT);

        context.fireChannelInactive();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
    	
    	Channel channel = context.channel();
    	
    	CatServerManager.INSTANCE.unlock(channel);

    	String deviceNo = CatServerManager.INSTANCE.getDeviceNoBy(context.channel());
    	
    	logger.error("[deviceNo:{}]:网络异常", deviceNo, cause);

    	context.fireExceptionCaught(cause);
    }

}