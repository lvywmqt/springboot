package com.cgltech.cat_conn.service;

import com.cgltech.cat_conn.service.dto.AddTerminalTicketResultDto;
import com.cgltech.cat_conn.service.dto.AddTicketCutDto;

/**
 * 终端本地出票结果
 *
 * @author
 * @create 2018-11-09 10:52
 **/
public interface TerminalTicketResultService {
    /**
     * 保存终端同步出票记录
     * @param addTerminalTicketResultDto
     * @return
     */
    public void addTerminalTicketResult(AddTerminalTicketResultDto addTerminalTicketResultDto);
}