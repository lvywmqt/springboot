package com.cgltech.cat_conn.service.impl;

import com.cgltech.cat_conn.dal.common.TicketCutResponseStatus;
import com.cgltech.cat_conn.dal.common.TicketCutStatus;
import com.cgltech.cat_conn.dal.dao.mybatis.mapper.TerminalTicketResultMapper;
import com.cgltech.cat_conn.dal.entity.TerminalTicketResult;
import com.cgltech.cat_conn.dal.entity.TicketCut;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;
import com.cgltech.cat_conn.exception.ServiceException;
import com.cgltech.cat_conn.service.TerminalTicketResultService;
import com.cgltech.cat_conn.service.dto.AddTerminalTicketResultDto;
import com.cgltech.cat_conn.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 终端出票结果
 *
 * @author
 * @create 2018-11-12 17:15
 **/
public class TerminalTicketResultServiceImpl implements TerminalTicketResultService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalTicketResultServiceImpl.class);

    @Autowired
    private TerminalTicketResultMapper terminalTicketResultMapper;

    /**
     * 保存终端同步出票记录
     *
     * @param addTerminalTicketResultDto
     */
    @Override
    public void addTerminalTicketResult(AddTerminalTicketResultDto addTerminalTicketResultDto) {
        TerminalTicketResult terminalTicketResult = new TerminalTicketResult();

        terminalTicketResult.setDeviceNo(addTerminalTicketResultDto.getDeviceNo());
        terminalTicketResult.setOrderNo(addTerminalTicketResultDto.getOrderNo());
        terminalTicketResult.setRequestSaleNum(addTerminalTicketResultDto.getRequestSaleNum());
        terminalTicketResult.setErrorCode(addTerminalTicketResultDto.getErrorCode());
        terminalTicketResult.setPlateState(addTerminalTicketResultDto.getPlateState());
        terminalTicketResult.setSignalQuality(addTerminalTicketResultDto.getSignalQuality());
        terminalTicketResult.setVoltage(addTerminalTicketResultDto.getVoltage());
        terminalTicketResult.setRealSaleNum(addTerminalTicketResultDto.getRealSaleNum());


        terminalTicketResult.setId(UuidUtil.get32UUID());

        try {
            TerminalTicketResult result = terminalTicketResultMapper.selectOne(terminalTicketResult.getOrderNo());

            //订单不存在则保存到数据库
            if (result == null) {
                terminalTicketResultMapper.insert(terminalTicketResult);
            }
        } catch (Exception e) {
            LOGGER.error("[deviceNo:{}]:添加终端出票结果错误:[{}]", terminalTicketResult.getDeviceNo(), terminalTicketResult, e);
            throw new ServiceException("添加终端出票结果错误");
        }

    }
}