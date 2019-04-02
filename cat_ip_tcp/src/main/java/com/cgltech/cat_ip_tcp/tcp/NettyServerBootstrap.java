package com.cgltech.cat_ip_tcp.tcp;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import com.cgltech.cat_ip_tcp.handle.SingletonProperty;
import com.cgltech.cat_ip_tcp.util.MyLog;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;



public class NettyServerBootstrap implements Runnable{
    private int port;

    public NettyServerBootstrap(int port) throws InterruptedException {
        this.port = port;
        
    }
    @Override
    public void run() {
    	bind();
    }
    private void bind() {
      
    	EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{       
	        ServerBootstrap bootstrap = new ServerBootstrap();
	        bootstrap.group(bossGroup,workerGroup);
	        bootstrap.channel(NioServerSocketChannel.class);
	        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
	        bootstrap.option(ChannelOption.TCP_NODELAY, true);
	        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
	        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
	            @Override
	            protected void initChannel(SocketChannel socketChannel) throws Exception {
	            	
				  ChannelPipeline cP = socketChannel.pipeline();
				  

	                    
	              // 以("\r\n"或"\n")为结尾分割的解码器
	              cP.addLast("frameDecoder", new DelimiterBasedFrameDecoder(10240, Delimiters.lineDelimiter()));
	
	              // 字符串解码 和 编码
	              cP.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
	              cP.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
	
	              // 自己的逻辑Handler
	              cP.addLast("serverHandler", new MyChannelHandler());
	            }
	        });
	        //绑定端口，同步等待成功
	        ChannelFuture f = null;
			try {
				f = bootstrap.bind(port).sync();
			} catch (InterruptedException e) {
				MyLog.error("绑定端口异常!"+e);
			}
	        if(f.isSuccess()){
	            MyLog.info("server start at port:"+port+"---------------");
	        }
	        //等待服务端监听端口关闭  
            try {
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				MyLog.error("等待服务端监听端口关闭  异常!"+e);
			}
        }finally {  
	        //优雅退出，释放线程池资源  
	        workerGroup.shutdownGracefully();  
	        bossGroup.shutdownGracefully();  
        }  
    }
    public static void main(String []args) throws InterruptedException {
    	String tcp_port = SingletonProperty.getInstance().getPropertyValue("systemConfig.tcp.port");
        NettyServerBootstrap nsb=new NettyServerBootstrap(Integer.parseInt(tcp_port));
        Thread th1 = new Thread(nsb);
        th1.start();

    }
}
