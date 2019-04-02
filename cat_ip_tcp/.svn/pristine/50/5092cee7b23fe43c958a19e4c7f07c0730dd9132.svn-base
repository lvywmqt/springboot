package com.cgltech.cat_ip_tcp.handle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cgltech.cat_ip_tcp.util.MyLog;


//此类功能为读取配置文件，通过内部类实现单例模式。
public class SingletonProperty {  
	public static void main(String[] args) {
//		SingletonProperty sp = SingletonProperty.getInstance(); 	
//		String ttt =  sp.getPropertyValue("log4j.rootLogger");
//		MyLog.info(String.valueOf(port));
//		MyLog.info(test);
	}
      
    private Properties props = null;  
      
    private static class ConfigurationHolder{  
        private static SingletonProperty configuration = new SingletonProperty();  
    }  
      
    public static SingletonProperty getInstance(){  
        return ConfigurationHolder.configuration;  
    }  
      
    public String getPropertyValue(String key){  
        return props.getProperty(key);  
    }  
      
    private SingletonProperty(){  
        readConfig();  
    }  
      
  
  
	private void readConfig() {  

    	 MyLog.info("开始加载properties文件内容.......");
         props = new Properties();
         InputStream in = null;
         InputStream in2 = null;
         try {
        	 //第一种，通过类加载器进行获取properties文件流
        	 in = SingletonProperty.class.getClassLoader().getResourceAsStream("systemConfig.properties");
//        	 in2 = SingletonProperty.class.getClassLoader().getResourceAsStream("redis.properties");
        	 
        	 //第二种，通过类进行获取properties文件流
             //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
             
        	 props.load(in);
//             props.load(in2);
         } catch (FileNotFoundException e) {
             MyLog.info("properties文件未找到");
         } catch (IOException e) {
             MyLog.info("出现IOException");
         } finally {
             try {
                 if(null != in) {
                     in.close();
                 }
             } catch (IOException e) {
                 MyLog.info("文件流关闭出现异常");
             }
         }
         MyLog.info("加载properties文件内容完成...........");
         MyLog.info("properties文件内容：" + props);
    }  
}  