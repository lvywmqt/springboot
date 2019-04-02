package com.cgltech.cat_conn.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cgltech.cat_conn.dal.dao.mybatis.mapper.TicketCutDetailsMapper;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;

/**
 * 出票超时修改ticket_cut状态定时任务
 * @author User
 *
 */
public class CutTicketTimeOutHandle {
    private static final Logger logger = LoggerFactory.getLogger(CutTicketTimeOutHandle.class);

	@Value("${systemConfig.ticketcut.timeout}")
	private String timeOut;

    @Autowired
    private TicketCutDetailsMapper<TicketCutDetails> ticketCutDetailsMapper;

    public void run(){
    	
		LocalDateTime dateTime = LocalDateTime.now();
		
		//LocalDateTime timeOutTime = dateTime.minusMinutes(Long.parseLong(timeOut));
		LocalDateTime timeOutTime = dateTime.minusSeconds(Long.parseLong(timeOut));
    	Date timeout = Date.from(timeOutTime.atZone(ZoneId.systemDefault()).toInstant());
    	
    	Date updateTime = new Date();
    	try{
    		logger.info("Task-出票超时处理开始,执行时间[{}],超时时间[{}]", updateTime, timeout);
        	int num = ticketCutDetailsMapper.updateStatusToTimeOut(timeout, updateTime);
        	logger.info("Task-出票超时处理结束,完成时间[{}],超时数量[{}]", updateTime, num);
    	}catch (Exception e){
        	logger.error("Task-出票超时处理异常：当前时间[{}]", updateTime,e);
    	}
  
    }

}