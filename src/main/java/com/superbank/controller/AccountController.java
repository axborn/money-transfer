package com.superbank.controller;

import com.superbank.exceptions.ValidationException;
import com.superbank.model.Account;

public class AccountController {

	public AccountController () {
		
	}
	
	public Account getAccount(String id) {
		Account account = new Account();
		account.setId(id);
		return account;
	}

	public Account insertAccount(Account account) throws ValidationException {
		AccountValidator.validateAccountBeforeCreation(account);
		
		account.setId("123321");
		
		return account;
	}
	
	

}
