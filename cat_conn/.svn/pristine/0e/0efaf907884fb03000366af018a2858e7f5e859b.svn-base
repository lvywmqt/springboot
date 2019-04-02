package com.cgltech.cat_conn.dal.dao.mybatis.mapper;

import com.cgltech.cat_conn.dal.dao.mybatis.CrudMapper;
import com.cgltech.cat_conn.dal.entity.TerminalTicketResult;
import com.cgltech.cat_conn.dal.entity.TicketCut;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public interface TerminalTicketResultMapper<T extends Serializable> extends CrudMapper<T> {

    TerminalTicketResult selectOne(String orderNo)throws DataAccessException;

}