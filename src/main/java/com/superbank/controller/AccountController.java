package com.superbank.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.superbank.dao.AccountDao;
import com.superbank.dao.model.Account;
import com.superbank.exceptions.DaoException;
import com.superbank.exceptions.ValidationException;

public class AccountController {

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);
	private AccountDao accountDao = new AccountDao();
	
	public AccountController () {
		
	}

	public List<Account> getAllAccounts() throws DaoException {
		return accountDao.getAll();
	}
	
	public Account getAccount(int id) throws DaoException {
		return accountDao.get(id);
	}

	public Account insertAccount(Account account) throws ValidationException, DaoException {
		AccountValidator.validateBeforeCreation(account);
		
		return accountDao.insert(account);
	}
	
	

}
