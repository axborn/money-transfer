/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.superbank;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import com.superbank.controller.AccountController;
import com.superbank.controller.TransactionController;
import com.superbank.dao.model.Account;
import com.superbank.dao.model.Transaction;
import com.superbank.exceptions.DaoException;
import com.superbank.exceptions.ValidationException;
import com.superbank.utils.ConversionUtils;

public class FinTechUnicornApplication {

	private static final Logger LOGGER = LogManager.getLogger(FinTechUnicornApplication.class);

	public static void main(String[] args) {

		AccountController accountController = new AccountController();
		TransactionController transactionController = new TransactionController();

		LOGGER.info("START");

		before("/*", (request, response) -> 
			LOGGER.info("Received API call " + request.requestMethod() + " to path: " + request.uri()));

		get("/accounts", (request, response) -> {
			try {
				List<Account> accounts = accountController.getAllAccounts();
				return ConversionUtils.toJson(accounts);
			} catch (DaoException daoException) {
				response.status(HttpStatus.NOT_FOUND_404);
				return daoException.getMessage();
			}
		});

		get("/account/:accountNumber", (request, response) -> {
			try {
				Optional<Account> account = accountController.getAccount(Integer.parseInt(request.params("accountNumber")));
				return ConversionUtils.toJson(account);

			} catch (DaoException daoException) {
				response.status(HttpStatus.NOT_FOUND_404);
				return daoException.getMessage();
			} catch (NumberFormatException nfException) {
				response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
				return nfException.getMessage();
			}
			
		});

		post("/account", (request, response) -> {
			Account requestAccount = ConversionUtils.fromJson(request.body(), Account.class);

			try {
				Account createdAccount = accountController.insertAccount(requestAccount);
				response.status(HttpStatus.CREATED_201);
				return ConversionUtils.toJson(createdAccount);
			} catch (ValidationException validationException) {
				response.status(HttpStatus.BAD_REQUEST_400);
				return validationException.getMessage();
			}
		});
		

		get("/transactions/:accountNumber", (request, response) -> {
			try {
				List<Transaction> accounts = transactionController.getAllTransactions(Integer.parseInt(request.params("accountNumber")));
				return ConversionUtils.toJson(accounts);
			} catch (DaoException daoException) {
				response.status(HttpStatus.NOT_FOUND_404);
				return daoException.getMessage();
			} catch (NumberFormatException nfException) {
				response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
				return nfException.getMessage();
			}
		});

		post("/transaction", (request, response) -> {
			Transaction requestTransaction = ConversionUtils.fromJson(request.body(), Transaction.class);

			try {
				Transaction createdTransaction = transactionController.insertTransaction(requestTransaction);
				response.status(HttpStatus.CREATED_201);
				return ConversionUtils.toJson(createdTransaction);
			} catch (ValidationException validationException) {
				response.status(HttpStatus.BAD_REQUEST_400);
				return validationException.getMessage();
			}
		});

	}

}
