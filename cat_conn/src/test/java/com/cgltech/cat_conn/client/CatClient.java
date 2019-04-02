package com.cgltech.cat_conn.client;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;  

public class CatClient {  
    
	private static final Logger log = LoggerFactory.getLogger(CatClient.class);
	
	private static final String HOSTNAME = "47.104.241.75";

    private static final int PORT = 9201;

    private static final int READER_IDLE_TIME = 0;

    private static final int WRITER_IDLE_TIME = 0;

    private static final int ALL_IDLE_TIME = 0;
	
    private static final int MAX_FRAME_LENGTH = 10240;
    
    private static Bootstrap bootstrap = null;  
    private static EventLoopGroup workerGroup = null; 
    static{
    	bootstrap = new Bootstrap();
    	workerGroup = new NioEventLoopGroup(1);
    	bootstrap.group(workerGroup).channel(NioSocketChannel.class)
    	.option(ChannelOption.SO_KEEPALIVE, true);
    }
    public static void init(CatClientHandler catClientIntHandler){
    	bootstrap.handler(new ChannelInitializer<SocketChannel>() {  
            @Override  
            public void initChannel(SocketChannel ch) throws Exception {  
            	ChannelPipeline cP = ch.pipeline();
  				  
				  	//采用配置文件配置IdleStateHandler的参数
				  	cP.addLast("idleStateHandler", new IdleStateHandler(
						  READER_IDLE_TIME, WRITER_IDLE_TIME, ALL_IDLE_TIME,TimeUnit.SECONDS));
	                    
				  	// 以("\r\n"或"\n")为结尾分割的解码器
				  	cP.addLast("frameDecoder", new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, 
	            		  Delimiters.lineDelimiter()));
	
				  	// 字符串解码 和 编码
				  	cP.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
				  	cP.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
	
				  	// 自己的逻辑Handler
				  	cP.addLast("serverHandler", catClientIntHandler);
            }  
        });
    }
	public static void connect(){
        try {  
            // Start the client.  
            ChannelFuture f = bootstrap.connect(HOSTNAME, PORT).sync();  
            
            // Wait until the connection is closed.  
            f.channel().closeFuture().sync();
            
        }catch (InterruptedException e) {
        	log.error("Failed to connect.", e);
		}catch (Exception e) {
        	log.error("Failed to connect.", e);
		} finally {
//            workerGroup.shutdownGracefully();
//            System.exit(0);
        }  
    }	
}  