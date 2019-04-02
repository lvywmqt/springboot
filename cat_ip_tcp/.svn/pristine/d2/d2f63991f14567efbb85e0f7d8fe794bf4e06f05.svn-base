package com.cgltech.cat_ip_tcp.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgltech.cat_ip_tcp.handle.DruidJdbcUtils;
import com.cgltech.cat_ip_tcp.tcp.MyChannelHandler;

public class Database_connection_test {
	private static Logger logger = LoggerFactory
            .getLogger(Database_connection_test.class);
	
	//测试连接数据库并执行测试sql
	public static void connnectionTest(){
		
		final String sql = "select now();";
		logger.info("prepare test connect and query mysql database,the sql is:{}",sql);
		List<Object> list = DruidJdbcUtils.executeQuery(sql, new Object[]{});
		logger.info("executeQueryResult:{}",list);
	}
	public static void main(String[] args){
		connnectionTest();
	}
	
}
