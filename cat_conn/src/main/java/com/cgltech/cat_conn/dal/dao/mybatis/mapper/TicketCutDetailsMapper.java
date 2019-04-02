package com.cgltech.cat_conn.dal.dao.mybatis.mapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.cgltech.cat_conn.dal.dao.mybatis.CrudMapper;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;

@Repository
public interface TicketCutDetailsMapper<T extends Serializable> extends CrudMapper<T> {

    void batchInsert(List<TicketCutDetails> TicketCutDetails) throws DataAccessException;

    void updateTicketCutResult(TicketCutDetails ticketCutDetails);

    List<TicketCutDetails> selectOriginalResult() throws DataAccessException;

    int updateStatus(
            @Param("id") String id,
            @Param("oldStatus") int oldStatus,
            @Param("newStatus") int newStatus,
            @Param("updateTime") Date updateTime) throws DataAccessException;

    int updateStatusToTimeOut(@Param("timeOut") Date timeOut, @Param("updateTime") Date updateTime) throws DataAccessException;

}