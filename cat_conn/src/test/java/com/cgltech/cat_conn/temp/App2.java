package com.cgltech.cat_conn.temp;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;  
/**
 * Hello world!
 *
 */
public class App2 
{
    public static void main( String[] args )
    {
    	for(int i = 1;i<10;i++){
    		
    		System.out.println(System.nanoTime());
//    		System.out.println(System.currentTimeMillis());
    	}
//    	System.out.println(System.currentTimeMillis());
//    	52024193668464
    }
    public static void testM(String a){
    	System.out.println(a);
    	App app = new com.cgltech.cat_conn.temp.App();
    	app.test();
    }
    /** 
     * 20位末尾的数字id 
     */  
    public static int Guid=100;  

    public static String getGuid() {  
          
    	App2.Guid+=1;  

        long now = System.currentTimeMillis();    
        //获取4位年份数字    
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");    
        //获取时间戳    
        String time=dateFormat.format(now);  
        String info=now+"";  
        //获取三位随机数    
        //int ran=(int) ((Math.random()*9+1)*100);   
        //要是一段时间内的数据连过大会有重复的情况，所以做以下修改  
        int ran=0;  
        if(App2.Guid>999){  
        	App2.Guid=100;         
        }  
        ran=GetTime.Guid;  
                  
        return time+info.substring(2, info.length())+ran;    
    }  
}
