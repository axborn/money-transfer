package com.superbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.superbank.dao.ds.DataSource;
import com.superbank.dao.model.Transaction;
import com.superbank.exceptions.DaoException;

public class TransactionDao {

	private static final Logger LOGGER = LogManager.getLogger(TransactionDao.class);

	private String SQL_QUERY_GETALL = "select * from TRANSACTIONS where FROM_transaction = ? OR TO_transaction = ?";

	public TransactionDao() {

	}

	public List<Transaction> getAll(int accountNumber) throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_GETALL);

		List<Transaction> transactions = null;
		try (Connection con = DataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_QUERY_GETALL);) {

			statement.setInt(1, accountNumber);
			statement.setInt(2, accountNumber);
			ResultSet rs = statement.executeQuery();

			transactions = new ArrayList<>();
			Transaction transaction;
			while (rs.next()) {
				transaction = new Transaction();
				transaction.setId(rs.getInt("ID"));
				transaction.setId(rs.getInt("FROM_ACCOUNT"));
				transaction.setId(rs.getInt("TO_ACCOUNT"));
				transaction.setBalance(rs.getBigDecimal("BALANCE"));
				transaction.setStatus(rs.getString("STATUS"));
				transactions.add(transaction);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return transactions;
	}
}
