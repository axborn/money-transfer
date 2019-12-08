package com.superbank.controller;

import com.superbank.dao.model.Account;
import com.superbank.exceptions.ValidationException;

import spark.utils.StringUtils;

public class AccountValidator {

	static boolean validateBeforeCreation(Account account) throws ValidationException {
		String exceptions = "";
		if (StringUtils.isEmpty(account.getCurrency()))
			exceptions += ("Empty currency; ");
		if (StringUtils.isEmpty(account.getEmail()))
			exceptions += ("Empty email; ");
		if (StringUtils.isEmpty(account.getPhone()))
			exceptions += ("Empty phone; ");
			
		if (StringUtils.isEmpty(exceptions))
			return true;
		else
			throw new ValidationException(exceptions);
	}

}
