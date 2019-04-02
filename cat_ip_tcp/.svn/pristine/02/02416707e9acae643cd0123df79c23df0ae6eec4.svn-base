package com.cgltech.cat_ip_tcp;

import com.cgltech.cat_ip_tcp.handle.SingletonProperty;
import com.cgltech.cat_ip_tcp.tcp.NettyServerBootstrap;

public class Main {

    public static void main(String[] args) throws InterruptedException {
    	//动态配置log4j的配置文件,参数可以是一个properties文件所在路径的String对象   
        //可以是一个properties文件所在路径的URL对象，也可以是一个properties对象   
//    	InputStream in = Main.class.getResourceAsStream("/resources/my_log4j.properties");
//    	if(in==null){
//    		in = Main.class.getResourceAsStream("/my_log4j.properties");
//    	}
////    	InputStream in = Main.class.getClassLoader().getResourceAsStream("/resources/my_log4j.properties");
//    	PropertyConfigurator.configure(in);
    	SingletonProperty sp = SingletonProperty.getInstance(); 	
		String tcp_port = sp.getPropertyValue("systemConfig.tcp.port");	
        
		//NettyServerBootstrap线程
		NettyServerBootstrap nsb = new NettyServerBootstrap(Integer.parseInt(tcp_port));
        Thread th1 = new Thread(nsb);
        th1.start();

    }
}
