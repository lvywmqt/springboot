package com.cgltech.cat_conn.listener;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cgltech.cat_conn.util.SingletonProperty;

public enum ApplicationContextFactory {
	
	INSTANCE();
	
	private AbstractApplicationContext appContext;
	
	private ApplicationContextFactory() {
		String isTest = SingletonProperty.getInstance().getPropertyValue("systemConfig.isTest");
		String YES = "1";
    	if (YES.equals(isTest)) {
    		appContext = new ClassPathXmlApplicationContext(
    				new String[]{
    						"/spring/applicationContext-main.xml",
    						"/spring/applicationContext-server.xml"
    						});
		}else{
			appContext = new ClassPathXmlApplicationContext("/spring/applicationContext-list.xml");
		}
	}

	public <T> T getService(Class<T> clz) {
		return (T)appContext.getBean(clz);
	}
}