/**
 * 
* 类名称： TicketCutServiceImpl.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年6月2日
* @version 2.0
 */
package com.cgltech.cat_conn.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgltech.cat_conn.dal.common.TicketCutDetailsCutTicketStatus;
import com.cgltech.cat_conn.dal.common.TicketCutDetailsStatus;
import com.cgltech.cat_conn.dal.common.TicketCutResponseStatus;
import com.cgltech.cat_conn.dal.common.TicketCutStatus;
import com.cgltech.cat_conn.dal.dao.mybatis.mapper.TicketCutDetailsMapper;
import com.cgltech.cat_conn.dal.dao.mybatis.mapper.TicketCutMapper;
import com.cgltech.cat_conn.dal.entity.TicketCut;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;
import com.cgltech.cat_conn.exception.ServiceException;
import com.cgltech.cat_conn.service.TicketCutService;
import com.cgltech.cat_conn.service.dto.AddTicketCutDto;
import com.cgltech.cat_conn.util.UuidUtil;

/**
 * @author User
 *
 */
@Service
@Transactional()
public class TicketCutServiceImpl implements TicketCutService{

	private static final Logger logger = LoggerFactory.getLogger(TicketCutServiceImpl.class);
	
	private static long TICKET_CUT_DETAILS_SERIAL_NUM = System.currentTimeMillis(); //初始化详情序号
	
	@Autowired
	TicketCutMapper<TicketCut> ticketCutMapper;

	@Autowired
	TicketCutDetailsMapper<TicketCut> ticketCutDetailsMapper;

	@Value("${systemConfig.each.cutTicket.value}")
	private Integer eachCutTicketValue;
	
	@Override
	public void addTicketCut(AddTicketCutDto addTicketCutDto) {
		
		TicketCut ticketCut = new TicketCut();
		
		ticketCut.setDeviceNo(addTicketCutDto.getDeviceNo());
		ticketCut.setMethod(addTicketCutDto.getMethod());
		ticketCut.setOrderNo(addTicketCutDto.getOrderNo());
		ticketCut.setRequestSaleNum(addTicketCutDto.getRequestSaleNum());
		ticketCut.setTimestamp(addTicketCutDto.getTimestamp());
		ticketCut.setNotifyUrl(addTicketCutDto.getNotifyUrl());
		
		ticketCut.setResponseStatus(TicketCutResponseStatus.NORMAL.getValue());
		ticketCut.setStatus(TicketCutStatus.NORMAL.getValue());
		
		int defaultScuccessSaleNum = 0;
		ticketCut.setSuccessSaleNum(defaultScuccessSaleNum);
		ticketCut.setUpdateTime(new Date());
		ticketCut.setCreateTime(new Date());
		ticketCut.setId(UuidUtil.get32UUID());

		try {
			TicketCut dbTicketCut = ticketCutMapper.selectOne(addTicketCutDto.getOrderNo());
			
			//订单不存在则保存到数据库
			if (dbTicketCut == null) {
				ticketCutMapper.insert(ticketCut);
				
				List<TicketCutDetails> ticketCutDetailsList = generateDetails(ticketCut,addTicketCutDto.getTicketLength()+"");

				ticketCutDetailsMapper.batchInsert(ticketCutDetailsList);
			}
		} catch (Exception e) {
			logger.error("[deviceNo:{}]:增加出票订单错误:[{}]", ticketCut.getDeviceNo(), ticketCut, e);
			throw new ServiceException("增加出票订单错误");
		}
	}

	/**
	 * 根据配置eachCutTicketValue(每条详情的票数)拆分订单，生成订单详情
	 * @param ticketCut
	 * @param ticketLength
	 * @return
	 */
	public List<TicketCutDetails> generateDetails(TicketCut ticketCut,String ticketLength){
		List<TicketCutDetails> ticketCutDetailsList = new ArrayList<>();
		
		int total = ticketCut.getRequestSaleNum();

		while(total > 0){
			//每条详情的票数，0表示不拆分
			int requestSaleNum = eachCutTicketValue == 0? total : eachCutTicketValue;
			//总票数小于配置的每条详情的票数时，设置详情票数
			if (total < eachCutTicketValue) {
				requestSaleNum = total;
			}
			//计算剩余未拆分详情票数
			total -= requestSaleNum;

			TicketCutDetails ticketCutDetails = generateTicketCutDetails(ticketCut, requestSaleNum,ticketLength);
			
			ticketCutDetailsList.add(ticketCutDetails);
		}
		return ticketCutDetailsList;
	}
	
	/**
	 * 生成详情数据
	 * @param ticketCut
	 * @param requestSaleNum
	 * @param ticketLength
	 * @return
	 */
	private TicketCutDetails generateTicketCutDetails(TicketCut ticketCut, int requestSaleNum,String ticketLength){
		TicketCutDetails ticketCutDetails = new TicketCutDetails();
		
		ticketCutDetails.setId(UuidUtil.get32UUID());
		ticketCutDetails.setTicketCutId(ticketCut.getId());
		ticketCutDetails.setOrderNo(ticketCut.getOrderNo());
		ticketCutDetails.setTicketLength(ticketLength);
		ticketCutDetails.setDeviceNo(ticketCut.getDeviceNo());
		
		ticketCutDetails.setSerialNum(TICKET_CUT_DETAILS_SERIAL_NUM++);
		
		ticketCutDetails.setCutTicketStatus(TicketCutDetailsCutTicketStatus.NORMAL.getValue());
		ticketCutDetails.setStatus(TicketCutDetailsStatus.NORMAL.getValue());

		ticketCutDetails.setRequestSaleNum(requestSaleNum);
		int defaultSuccessSaleNum = 0;
		ticketCutDetails.setSuccessSaleNum(defaultSuccessSaleNum);
//		ticketCutDetails.setTimestamp(timestamp);
		
		ticketCutDetails.setCreateTime(new Date());
		ticketCutDetails.setUpdateTime(new Date());
		
		return ticketCutDetails;
	}

}
