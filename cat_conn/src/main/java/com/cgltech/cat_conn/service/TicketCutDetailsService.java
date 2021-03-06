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

import java.util.List;

import com.cgltech.cat_conn.dal.entity.TicketCutDetails;
import com.cgltech.cat_conn.service.dto.UpdateTicketCutResultDto;

/**
 * 修改通讯记录详情表
 */
public interface TicketCutDetailsService {
	
	public void updateTicketCutResult(UpdateTicketCutResultDto updateTicketCutResultDto);
	
	public List<TicketCutDetails> selectTicketCutDetails();
	
	/**
	 * 出票状态修改为出票中
	 * @param id
	 * @return
	 */
	public int updateStatusInitToCuting(String id);
    
	/**
	 * 出票状态修改为初始状态
	 * @param id
	 * @return
	 */
	public int updateStatusCutingToInit(String id);
    
	/**
	 * 出票状态修改为出票失败
	 * @param id
	 * @return
	 */
    public int updateStatusCutingToFailed(String id);

	/**
	 * 出票状态修改为出票失败
	 * @param id
	 * @return
	 */
	public int updateStatusInitToFailed(String id);

}
