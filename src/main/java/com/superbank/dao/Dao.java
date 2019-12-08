package com.superbank.dao;

import java.util.List;
import java.util.Optional;

import com.superbank.exceptions.DaoException;

public interface Dao<T> {
    
    Optional<T> get(int id) throws DaoException;
     
    List<T> getAll() throws DaoException;
     
    T insert(T t) throws DaoException;
     
    void update(T t, String[] params);
     
    void delete(T t);
}
