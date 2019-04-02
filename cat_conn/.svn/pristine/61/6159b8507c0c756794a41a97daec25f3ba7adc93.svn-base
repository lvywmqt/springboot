/**
 * 类名称： TicketCutServiceImpl.java
 * 类描述：
 *
 * @author Li xiao jun
 * 作者单位： 中竞
 * 联系方式：
 * 修改时间：2018年6月2日
 * @version 2.0
 */
package com.cgltech.cat_conn.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgltech.cat_conn.dal.common.TicketCutDetailsCutTicketStatus;
import com.cgltech.cat_conn.dal.common.TicketCutDetailsStatus;
import com.cgltech.cat_conn.dal.dao.mybatis.mapper.TicketCutDetailsMapper;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;
import com.cgltech.cat_conn.exception.ServiceException;
import com.cgltech.cat_conn.service.TicketCutDetailsService;
import com.cgltech.cat_conn.service.dto.UpdateTicketCutResultDto;

/**
 * @author User
 */
@Service
@Transactional()
public class TicketCutDetailsServiceImpl implements TicketCutDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(TicketCutDetailsServiceImpl.class);

    @Autowired
    TicketCutDetailsMapper<TicketCutDetails> ticketCutDetailsMapper;

    @Override
    public void updateTicketCutResult(UpdateTicketCutResultDto updateTicketCutResultDto) {
        try {

        	TicketCutDetails ticketCutDetails = new TicketCutDetails();
        	
			ticketCutDetails.setId(updateTicketCutResultDto.getOrderNo());
			ticketCutDetails.setSuccessSaleNum(updateTicketCutResultDto.getSuccessSaleNum());
			ticketCutDetails.setTimestamp(updateTicketCutResultDto.getTimestamp());
			ticketCutDetails.setErrcode(updateTicketCutResultDto.getErrcode());

			
			if (updateTicketCutResultDto.getRequestSaleNum() != null
					&& updateTicketCutResultDto.getSuccessSaleNum() != null
					&& updateTicketCutResultDto.getRequestSaleNum() == updateTicketCutResultDto.getSuccessSaleNum()) {
				ticketCutDetails.setStatus(TicketCutDetailsStatus.FINISHED.getValue());
			}else{
				ticketCutDetails.setStatus(TicketCutDetailsStatus.FAILED.getValue());
			}
        	ticketCutDetails.setCutTicketStatus(TicketCutDetailsCutTicketStatus.RESPONSE.getValue());

			ticketCutDetails.setUpdateTime(new Date());


            ticketCutDetailsMapper.updateTicketCutResult(ticketCutDetails);
        } catch (Exception e){
            logger.error("更新出票结果错误:[{}]", updateTicketCutResultDto, e);
            throw new ServiceException("更新出票结果错误");
        }

    }

    @Override
    public List<TicketCutDetails> selectTicketCutDetails() {
        try {
            return ticketCutDetailsMapper.selectOriginalResult();
        } catch (DataAccessException e) {
            logger.error("查询出票请求错误:{}", "", e);
            throw new ServiceException("查询出票请求错误");
        }
    }

	@Override
	public int updateStatusInitToCuting(String id) {
		
		return ticketCutDetailsMapper.updateStatus(id,
				TicketCutDetailsStatus.NORMAL.getValue(),
				TicketCutDetailsStatus.TICKET_CUTING.getValue(),
				new Date());
	}

	@Override
	public int updateStatusCutingToInit(String id) {
		
		return ticketCutDetailsMapper.updateStatus(id,
				TicketCutDetailsStatus.TICKET_CUTING.getValue(),
				TicketCutDetailsStatus.NORMAL.getValue(),
				new Date());
	}

	@Override
	public int updateStatusCutingToFailed(String id) {
		
		return ticketCutDetailsMapper.updateStatus(id,
				TicketCutDetailsStatus.TICKET_CUTING.getValue(),
				TicketCutDetailsStatus.FAILED.getValue(),
				new Date());
	}

	@Override
	public int updateStatusInitToFailed(String id) {

		return ticketCutDetailsMapper.updateStatus(id,
				TicketCutDetailsStatus.NORMAL.getValue(),
				TicketCutDetailsStatus.FAILED.getValue(),
				new Date());
	}

}

