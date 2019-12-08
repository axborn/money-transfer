package com.superbank.controller;

import com.superbank.dao.model.Transaction;
import com.superbank.exceptions.ValidationException;

import spark.utils.StringUtils;

public class TransactionValidator {

	static boolean validateBeforeCreation(Transaction transaction) throws ValidationException {
		String exceptions = "";
		if (StringUtils.isEmpty(transaction.getFromAccount()))
			exceptions += ("Empty origin account; ");
		if (StringUtils.isEmpty(transaction.getToAccount()))
			exceptions += ("Empty destination account; ");
		if (StringUtils.isEmpty(transaction.getCurrency()))
			exceptions += ("Empty currency; ");
		if (transaction.getAmount() == null)
			exceptions += ("Empty amount; ");
		if (StringUtils.isEmpty(transaction.getStatus()))
			exceptions += ("Empty status; ");
			
		if (StringUtils.isEmpty(exceptions))
			return true;
		else
			throw new ValidationException(exceptions);
	}

}