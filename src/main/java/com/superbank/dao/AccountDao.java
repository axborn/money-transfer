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
import com.superbank.dao.model.Account;
import com.superbank.exceptions.DaoException;

import spark.utils.StringUtils;

public class AccountDao {

	private static final Logger LOGGER = LogManager.getLogger(AccountDao.class);

	private String SQL_QUERY_GET = "select * from ACCOUNTS where ID = ?";
	private String SQL_QUERY_GETALL = "select * from ACCOUNTS";
	private String SQL_QUERY_INSERT = "insert into ACCOUNTS ( ID, STATUS, EMAIL, PHONE, IBAN, CURRENCY, BALANCE ) "
			+ "VALUES ( default, ?, ?, ?, ?, ?, ? )";
	private String SQL_QUERY_UPDATE_AMOUNT = "update ACCOUNTS set BALANCE = ? where ID = ?";

	public AccountDao() {

	}

	public Account get(int id) throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_GET);

		try (Connection con = DataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_QUERY_GET);) {

			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {

				Account account = new Account();
				account.setId(rs.getInt("ID"));
				account.setStatus(rs.getString("STATUS"));
				account.setEmail(rs.getString("EMAIL"));
				account.setPhone(rs.getString("PHONE"));
				account.setIban(rs.getString("IBAN"));
				account.setCurrency(rs.getString("CURRENCY"));
				account.setBalance(rs.getBigDecimal("BALANCE"));
				return account;
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		throw new DaoException("No account");
	}

	public List<Account> getAll() throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_GETALL);

		List<Account> accounts = null;
		try (Connection con = DataSource.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_QUERY_GETALL);
				ResultSet rs = pst.executeQuery();) {
			accounts = new ArrayList<>();
			Account account;
			while (rs.next()) {
				account = new Account();
				account.setId(rs.getInt("ID"));
				account.setStatus(rs.getString("STATUS"));
				account.setEmail(rs.getString("EMAIL"));
				account.setPhone(rs.getString("PHONE"));
				account.setIban(rs.getString("IBAN"));
				account.setCurrency(rs.getString("CURRENCY"));
				account.setBalance(rs.getBigDecimal("BALANCE"));
				accounts.add(account);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return accounts;
	}

	public Account insert(Account account) throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_INSERT);

		try (Connection con = DataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_QUERY_INSERT,
						Statement.RETURN_GENERATED_KEYS);) {
			if (StringUtils.isEmpty(account.getStatus()))
				statement.setString(1, "PENDING");
			else
				statement.setString(1, account.getStatus());
			statement.setString(2, account.getEmail());
			statement.setString(3, account.getPhone());
			if (StringUtils.isEmpty(account.getIban()))
				statement.setString(4, "LT" + account.getId());
			else
				statement.setString(4, account.getIban());
			statement.setString(5, account.getCurrency());
			if (account.getBalance() == null)
				statement.setBigDecimal(6, BigDecimal.ZERO);
			else
				statement.setBigDecimal(6, account.getBalance());

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new DaoException("Creating account failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					account.setId(generatedKeys.getInt(1));
				} else {
					throw new DaoException("Creating account failed, no ID obtained.");
				}
			}
			return account;
		} catch (SQLException se) {
			throw new DaoException(se);
		}
	}

	public void updateAmount(Account account) throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_UPDATE_AMOUNT);

		try (Connection con = DataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_QUERY_UPDATE_AMOUNT,
						Statement.RETURN_GENERATED_KEYS);) {
			statement.setBigDecimal(1, account.getBalance());
			statement.setInt(2, account.getId());

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new DaoException("Creating user failed, no rows affected.");
			}
		} catch (SQLException se) {
			throw new DaoException(se);
		}

	}
}
