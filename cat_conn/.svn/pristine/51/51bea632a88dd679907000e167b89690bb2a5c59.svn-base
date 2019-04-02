/**
 * 
* 类名称： TicketCutStatus.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年6月2日
* @version 2.0
 */
package com.cgltech.cat_conn.dal.common;

/**
 * @author User
 *
 */
public enum TicketCutStatus {
	
	NORMAL(0, "初始"),
	SUCCESS(1, "出票成功"),
	PART_SUCCESS(2, "部分成功"),
	FAILED(3,"出票失败");


	
	private int value;
	private String desc;
	
	private TicketCutStatus(int value, String desc){
		this.desc = desc;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
}
