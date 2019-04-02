package com.cgltech.cat_conn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 说明：统一日志处理
 * 创建人：
 * 
 * 
 */
//参考org.slf4j.impl.Log4jLoggerAdapter
//public class MyLog4jLoggerAdapter {
public class MyLog {
//	static{
//		InputStream in = MyLog.class.getResourceAsStream("/my_log4j.properties");
//		System.out.println(in==null);
//		PropertyConfigurator.configure(in);
//	}
	private static Logger logger = LoggerFactory.getLogger(MyLog.class);
	
	//logger.log时传递FQCN参数，以便于定位用户的调用类为当前类的上一层的类。
	final static String FQCN = MyLog.class .getName();
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static void debug(String msg) {
	    logger.debug(msg);
	}
	public static void debug(String msg,Throwable t) {
		logger.debug( msg, t);
	}

	public static void error(String msg) {
	    logger.error(msg);
	}
	
	public static void error(String msg,Throwable t) {
		logger.error(msg, t);
	}

	public static void info(String msg) {
	    logger.info(msg);
	}

	public static void warn(String msg) {
	    logger.warn(msg);
	}
}