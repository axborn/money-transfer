package com.superbank.dao;

import java.math.BigDecimal;
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

import spark.utils.StringUtils;

public class TransactionDao {

	private static final Logger LOGGER = LogManager.getLogger(TransactionDao.class);

	private String SQL_QUERY_GETALL = "select * from TRANSACTIONS where FROM_ACCOUNT = ? OR TO_ACCOUNT = ?";
	private String SQL_QUERY_INSERT = "insert into TRANSACTIONS ( ID, FROM_ACCOUNT, TO_ACCOUNT, CURRENCY, AMOUNT, STATUS ) "
			+ "VALUES ( default, ?, ?, ?, ?, ? )";

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
				transaction.setFromAccount(rs.getInt("FROM_ACCOUNT"));
				transaction.setToAccount(rs.getInt("TO_ACCOUNT"));
				transaction.setCurrency(rs.getString("CURRENCY"));
				transaction.setAmount(rs.getBigDecimal("AMOUNT"));
				transaction.setStatus(rs.getString("STATUS"));
				transactions.add(transaction);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return transactions;
	}

	public Transaction insert(Transaction transaction) throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_INSERT);

		try (Connection con = DataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_QUERY_INSERT,
						Statement.RETURN_GENERATED_KEYS);) {

			statement.setInt(1, transaction.getFromAccount());
			statement.setInt(2, transaction.getToAccount());
			statement.setString(3, transaction.getCurrency());
			statement.setBigDecimal(4, transaction.getAmount());
			statement.setString(5, transaction.getStatus());

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					transaction.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
			return transaction;
		} catch (SQLException se) {
			throw new DaoException(se);
		}
	}
}
