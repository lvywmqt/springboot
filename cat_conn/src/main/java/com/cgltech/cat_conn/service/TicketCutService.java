/**
 * 
* 类名称： TicketCutService.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年6月2日
* @version 2.0
 */
package com.cgltech.cat_conn.service;

import com.cgltech.cat_conn.service.dto.AddTicketCutDto;

/**
 * @author User
 *
 */
public interface TicketCutService {
	
	/**
	 * 保存出票订单
	 * @param addTicketCutDto
	 * @return
	 */
	public void addTicketCut(AddTicketCutDto addTicketCutDto);
}
