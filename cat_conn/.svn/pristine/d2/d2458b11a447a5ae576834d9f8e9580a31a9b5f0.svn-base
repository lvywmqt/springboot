package com.cgltech.cat_conn.client;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cgltech.cat_conn.client.handler.CatClientHandlerFactory;
import com.cgltech.cat_conn.client.handler.IHandleCatClient;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;  
  
@Sharable
public class CatClientHandler extends ChannelInboundHandlerAdapter {  
      
    private static Logger logger = LoggerFactory.getLogger(CatClientHandler.class);  
  
    private static final String HEARTBEAT_PING_SERVER = "a";
    private static final String HEARTBEAT_PONG_SERVER = "b";

    // 接收server端的消息，并打印出来  
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {  
		
    	String terminalCode = context.channel().attr(CatUploadDeviceStatusHandler.deviceNoKey).get();
    	logger.info("[deviceNo:{}]: receive data is [{}]", terminalCode, msg);
    	
    	if (HEARTBEAT_PING_SERVER.equals(msg)) {
    		
        	logger.info("[deviceNo:{}]: response data is [{}]", terminalCode, HEARTBEAT_PONG_SERVER);
    		context.channel().writeAndFlush(HEARTBEAT_PONG_SERVER + "\n");
    		return;
		}
    	TransmissionParameterVO tp = null;
		try {
			tp = JSONObject.parseObject(msg.toString(), TransmissionParameterVO.class);
		} catch (Exception e) {
			logger.error("[deviceNo:{}]: 接收参数转换为json出错,[{}]", terminalCode, msg, e);
	    	context.channel().writeAndFlush("[deviceNo:" + terminalCode + "]: 接收参数转换为json出错！");
			return;
		}
		IHandleCatClient handle = CatClientHandlerFactory.getCatHandler(tp.getMethod());
		
		String response = handle.handle(tp);

		//无返回内容
		if(StringUtils.isEmpty(response))
			return;
			
		logger.info("[deviceNo:{}]: client response data is [{}]", terminalCode, response);
		
		context.channel().writeAndFlush(response + "\n");
    }  
  
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    	String deviceNo = ctx.channel().attr(CatUploadDeviceStatusHandler.deviceNoKey).get();

    	logger.info("[deviceNo:{}]: server close channel.", deviceNo);
    	
    	ctx.channel().close();
    	super.channelInactive(ctx);
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:{
                    handleReaderIdle(ctx);
                    break;
                }
                case WRITER_IDLE:{
                    break;
                }
                case ALL_IDLE:{
                    break;
                }
            }
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	String deviceNo = ctx.channel().attr(CatUploadDeviceStatusHandler.deviceNoKey).get();
    	logger.error("[deviceNo:{}]: exceptionCaught.", deviceNo, cause);
    	super.exceptionCaught(ctx, cause);
    }
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
    	
    	String deviceNo = ctx.channel().attr(CatUploadDeviceStatusHandler.deviceNoKey).get();

    	logger.info("[deviceNo:{}]: read time out, close channel.", deviceNo);
    	
        ctx.channel().close();
    }
    
}  