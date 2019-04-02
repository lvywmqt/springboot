package com.cgltech.cat_conn;

import com.cgltech.cat_conn.listener.ApplicationContextFactory;
import com.cgltech.cat_conn.server.cat.CatServerBootstrap;
import com.cgltech.cat_conn.server.http.NettyHttpServerBootstrap;

public class Main {

    public static void main(String[] args) throws InterruptedException {
    	
    	NettyHttpServerBootstrap httpServerBootstrap = ApplicationContextFactory.INSTANCE.getService(NettyHttpServerBootstrap.class);
    	CatServerBootstrap nettyServerBootstrap = ApplicationContextFactory.INSTANCE.getService(CatServerBootstrap.class);

    	new Thread(() -> {
    		httpServerBootstrap.run();
    	}).start();
    	
    	new Thread(() -> {
    		nettyServerBootstrap.run();
    	}).start();
    }
}
