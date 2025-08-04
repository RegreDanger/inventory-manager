package infra.shared.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import kernel.exceptions.impl.InternalServerException;
import kernel.utils.enums.ErrorCode;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:sqlite:database.db";
    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
				connection = DriverManager.getConnection(URL);
			} catch (SQLException e) {
	        	throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
			}
        }
        return connection;
    }
}
