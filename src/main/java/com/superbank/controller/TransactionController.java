package com.superbank.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.util.StringUtils;

import com.superbank.dao.AccountDao;
import com.superbank.dao.TransactionDao;
import com.superbank.dao.model.Account;
import com.superbank.dao.model.Transaction;
import com.superbank.exceptions.DaoException;
import com.superbank.exceptions.ValidationException;

public class TransactionController {

	private static final Logger LOGGER = LogManager.getLogger(TransactionController.class);
	private TransactionDao transactionDao = new TransactionDao();
	private AccountDao accountDao = new AccountDao();
	
	public TransactionController () {
		
	}

	public List<Transaction> getAllTransactions(int accountNumber) throws DaoException {
		return transactionDao.getAll(accountNumber);
	}

	public Transaction insertTransaction(Transaction transaction) throws DaoException, ValidationException {
		TransactionValidator.validateBeforeCreation(transaction);
		if (StringUtils.equals("CLEARED", transaction.getStatus()))
			return transactionDao.insert(transaction);
		else if (StringUtils.equals("PENDING", transaction.getStatus())){
			transactionDao.insert(transaction);
			
			Account fromAccount = accountDao.get(transaction.getFromAccount());
			fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
			accountDao.updateAmount(fromAccount);
			
			Account toAccount = accountDao.get(transaction.getToAccount());
			toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
			accountDao.updateAmount(toAccount);
			
			transaction.setStatus("CLEARED");
			transactionDao.updateStatus(transaction);
			return transaction;
		}
		else
			throw new ValidationException("Unknown status: " + transaction.getStatus());
	}
	
	
	

}
