package com.cgltech.cat_conn.server.cat.handle.impl;

import com.alibaba.fastjson.JSONObject;
import com.cgltech.cat_conn.listener.ApplicationContextFactory;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.server.cat.handle.IHandleCatServer;
import com.cgltech.cat_conn.service.TerminalTicketResultService;
import com.cgltech.cat_conn.service.TicketCutDetailsService;
import com.cgltech.cat_conn.service.dto.AddTerminalTicketResultDto;
import com.cgltech.cat_conn.service.dto.UpdateTicketCutResultDto;
import com.cgltech.cat_conn.service.impl.TerminalTicketResultServiceImpl;
import com.cgltech.cat_conn.service.impl.TicketCutDetailsServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;

/**
 * @author
 * @create 2018-11-08 15:08
 **/
public class HandleForQueryResult implements IHandleCatServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleForQueryResult.class);

    @Override
    public String handle(ChannelHandlerContext context, TransmissionParameterVO transmissionParameterVO) {
        JSONObject data = new JSONObject(true);
        data.put("orderNo", transmissionParameterVO.getData().get("orderNo"));

        try {
            TerminalTicketResultService terminalTicketResultService = ApplicationContextFactory.INSTANCE.getService(TerminalTicketResultServiceImpl.class);

            AddTerminalTicketResultDto addTerminalTicketResultDto = new AddTerminalTicketResultDto();

            JSONObject requestdata = transmissionParameterVO.getData();

            addTerminalTicketResultDto.setDeviceNo(transmissionParameterVO.getDeviceNo());
            addTerminalTicketResultDto.setOrderNo(requestdata.getString("orderNo"));//主键
            addTerminalTicketResultDto.setRequestSaleNum(requestdata.getString("requestSaleNum"));
            addTerminalTicketResultDto.setErrorCode(requestdata.getString("errcode"));
            addTerminalTicketResultDto.setPlateState(requestdata.getString("plateState"));
            addTerminalTicketResultDto.setSignalQuality(requestdata.getString("signalQuality"));
            addTerminalTicketResultDto.setVoltage(requestdata.getString("voltage"));
            addTerminalTicketResultDto.setRealSaleNum(requestdata.getString("realSaleNum"));

            terminalTicketResultService.addTerminalTicketResult(addTerminalTicketResultDto);

        } catch (Exception e) {
            LOGGER.error("[deviceNo:{}]:查询终端出票结果错误[{}]", transmissionParameterVO.getDeviceNo(), transmissionParameterVO, e);
        }

        TransmissionParameterVO response = new TransmissionParameterVO();
        response.setDeviceNo(transmissionParameterVO.getDeviceNo());
        response.setMethod("roger_return_queryResult");
        response.setTime(String.valueOf(Clock.systemDefaultZone().millis()));
        response.setData(data);

        return JSONObject.toJSONString(response);
    }
}