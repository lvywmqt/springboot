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
public enum TicketCutDetailsStatus {

	NORMAL(0, "初始"),
	TICKET_CUTING(1, "出票中"),
	FINISHED(2, "出票成功"),
    FAILED(3,"出票失败"),
	UN_SEND_TIME_OUT(4,"未发送出票指令超时"),
	SENDED_TIME_OUT(5,"发送出票指令后超时");

	private int value;
	private String desc;

	private TicketCutDetailsStatus(int value, String desc){
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
