package com.cgltech.cat_conn.server.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cgltech.cat_conn.util.MyLog;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

@Component
public class NettyHttpServerBootstrap implements Runnable {

	/* 是否使用https协议 */
	static final boolean SSL = System.getProperty("ssl") != null;

	static SslContext sslCtx = null;

	@Value("${systemConfig.http.port}")
	private int port;

	public NettyHttpServerBootstrap(){}

	@Override
	public void run() {

		MyLog.info("=======进入NettyHttpServerBootstrap线程========");
		start();
	}

	public void start() {

		try {
			if (SSL) {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} else {
				sslCtx = null;
			}
		} catch (Exception e) {
			MyLog.error("", e);
		}

		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);

			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {

					ChannelPipeline p = ch.pipeline();

					if (sslCtx != null) {
						p.addLast(sslCtx.newHandler(ch.alloc()));
					}

					p.addLast(new HttpServerCodec());/* HTTP 服务的解码器 */

					p.addLast(new HttpObjectAggregator(2048));/* HTTP 消息的合并处理 */

					p.addLast(new NettyHttpServerHandler()); /* 自己写的服务器逻辑处理 */
				}
			});
			Channel ch = null;
			try {
				ch = bootstrap.bind(port).sync().channel();
				
				MyLog.info("http server start at port:" + port);
				
				ch.closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}
