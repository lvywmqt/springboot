package com.cgltech.cat_conn.dal.dao.mybatis.mapper;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.cgltech.cat_conn.dal.dao.mybatis.CrudMapper;
import com.cgltech.cat_conn.dal.entity.TicketCut;

@Repository
public interface TicketCutMapper<T extends Serializable> extends CrudMapper<T>{

    TicketCut selectOne(String orderNo)throws DataAccessException;

    List<TicketCut>selectCompleteTicketCut()throws DataAccessException;

    List<TicketCut> selectUnResponseTicketCut() throws DataAccessException;
    
	void updateResponseStatus(TicketCut ticketCut);
}