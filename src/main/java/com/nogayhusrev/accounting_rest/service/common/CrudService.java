package com.nogayhusrev.accounting_rest.service.common;

import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;

import java.util.List;

public interface CrudService<T, ID> {


    T findById(ID id) throws AccountingProjectException;

    List<T> findAll() throws AccountingProjectException;

    T findByName(String name) throws AccountingProjectException;

    void save(T t) throws AccountingProjectException;

    void delete(ID id) throws AccountingProjectException;

    void update(T t, ID id) throws AccountingProjectException;

    boolean isExist(T t, ID id) throws AccountingProjectException;

    boolean isExist(T t) throws AccountingProjectException;

}

