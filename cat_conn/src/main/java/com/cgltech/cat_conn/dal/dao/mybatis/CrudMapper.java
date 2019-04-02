package com.cgltech.cat_conn.dal.dao.mybatis;

import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.List;

public interface CrudMapper<T extends Serializable> {

    Long insert(T domain) throws DataAccessException;

    void delete(T domain) throws DataAccessException;

    void update(T domain) throws DataAccessException;

    List<T> selectList(T domain) throws DataAccessException;

}