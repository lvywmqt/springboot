package com.cgltech.cat_conn.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cgltech.cat_conn.dal.common.TicketCutStatus;
import com.cgltech.cat_conn.dal.dao.mybatis.mapper.TicketCutMapper;
import com.cgltech.cat_conn.dal.entity.TicketCut;

/**
 * 合并切票详情定时任务
 */
public class MergeTicketCutDetailsHandle {
    private static final Logger logger = LoggerFactory.getLogger(MergeTicketCutDetailsHandle.class);

    @Autowired
	TicketCutMapper<TicketCut> ticketCutMapper;
    
    public void mergeTicketCutDetails() {
        //定时合并出票成功的记录详情表到总表中
        logger.info("Task-合并出票成功的记录详情表到总表中开始");
		List<TicketCut> ticketCuts=ticketCutMapper.selectCompleteTicketCut();
		
        if(ticketCuts != null && ticketCuts.size() > 0){
			for (TicketCut ticketCut : ticketCuts) {
				try {
					ticketCut.setUpdateTime(new Date());
					if(ticketCut.getRequestSaleNum() == ticketCut.getSuccessSaleNum()){
						ticketCut.setStatus(TicketCutStatus.SUCCESS.getValue());
					}
					else if(ticketCut.getSuccessSaleNum() == 0){
						ticketCut.setStatus(TicketCutStatus.FAILED.getValue());
					}else{
						ticketCut.setStatus(TicketCutStatus.PART_SUCCESS.getValue());
					}
					ticketCutMapper.update(ticketCut);
				} catch (Exception e) {
					logger.error("Task-定时任务合并出票订单错误:[{}]", new Object[]{ticketCut,e});
				}
			}
		}
        logger.info("Task-合并出票成功的记录详情表到总表中结束");
    }
}