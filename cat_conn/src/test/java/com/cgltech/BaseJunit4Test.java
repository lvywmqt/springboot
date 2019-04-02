package com.cgltech;
/**
 * 
* 类名称： BaseJunit4Test.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月23日
* @version 2.0
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author User
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:spring/applicationContext-list.xml"
})
public class BaseJunit4Test {
	public BaseJunit4Test(){
		System.out.println(System.getenv());
	}
	
	@Test
	public void test(){
		
	}
}
