package com.cgltech.cat_conn.temp;

import java.lang.reflect.Method;

/**
 * Hello world!
 *
 */
public class App 
{	public void test(){
	

	
	Class c = this.getClass();//获得当前类的Class对象
    Method method = null;
    String methodName = "testM";
    try {
        //获得Method对象
        method =  c.getMethod(methodName, String.class);
    } catch (Exception e) {
        throw new RuntimeException("没有找到"+methodName+"方法，请检查该方法是否存在");
    }
     
    try {
        method.invoke(this,"4");//反射调用方法
    } catch (Exception e) {
        System.out.println("你调用的方法"+methodName+",内部发生了异常");
        throw new RuntimeException(e);
    }
	
	
	
//    System.out.println( "Hello World!" );

}
    public static void main( String[] args )
    {}
    public void testM(String a){
    	System.out.println(a);
    	
    }
}
