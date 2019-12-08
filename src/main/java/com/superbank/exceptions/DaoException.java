package com.superbank.exceptions;

import java.sql.SQLException;

public class DaoException extends Exception {

	public DaoException() {
        super();
    }
	
	public DaoException(String message) {
        super(message);
    }

    public DaoException(SQLException e) {
        super(e);
    }
	
}
