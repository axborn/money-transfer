package com.superbank.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.superbank.dao.TransactionDao;
import com.superbank.dao.model.Transaction;
import com.superbank.exceptions.DaoException;

public class TransactionController {

	private static final Logger LOGGER = LogManager.getLogger(TransactionController.class);
	private TransactionDao transactionDao = new TransactionDao();
	
	public TransactionController () {
		
	}

	public List<Transaction> getAllTransactions(int accountNumber) throws DaoException {
		return transactionDao.getAll(accountNumber);
	}
	
	
	

}
