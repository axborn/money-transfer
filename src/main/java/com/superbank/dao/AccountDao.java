package com.superbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.superbank.dao.jdbc.DataSource;
import com.superbank.dao.model.Account;
import com.superbank.exceptions.DaoException;

public class AccountDao implements Dao<Account> {

	private static final Logger LOGGER = LogManager.getLogger(AccountDao.class);

	private String SQL_QUERY_GET = "select * from ACCOUNTS where ID = ?";
	private String SQL_QUERY_GETALL = "select * from ACCOUNTS";
	private String SQL_QUERY_INSERT = "insert into ACCOUNTS ( ID, STATUS, EMAIL, PHONE, IBAN, CURRENCY, BALANCE ) "
			+ "VALUES ( default, ?, ?, ?, ?, ?, ? )";

	public AccountDao() {

	}

	@Override
	public Optional<Account> get(int id) throws DaoException {
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
				return Optional.of(account);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return Optional.empty();
	}

	@Override
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
				accounts.add(account);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return accounts;
	}

	@Override
	public Account insert(Account account) throws DaoException {
		LOGGER.info("Attempting " + SQL_QUERY_GETALL);

		try (Connection con = DataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_QUERY_INSERT,
						Statement.RETURN_GENERATED_KEYS);) {
			statement.setString(1, account.getStatus());
			statement.setString(2, account.getEmail());
			statement.setString(3, account.getPhone());
			statement.setString(4, account.getIban());
			statement.setString(5, account.getCurrency());
			statement.setString(6, account.getBalance());

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					account.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
			return account;
		} catch (SQLException se) {
			throw new DaoException(se);
		}
	}

	@Override
	public void update(Account Account, String[] params) {
		// TODO
	}

	@Override
	public void delete(Account Account) {
		// TODO
	}
}
