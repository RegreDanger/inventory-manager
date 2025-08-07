package infra.shared.config.db.sqlite;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import kernel.exceptions.impl.InternalServerException;
import kernel.utils.enums.ErrorCode;

public class DatabaseSqliteConnection {
    
    private static final String URL = "jdbc:sqlite:database.db";
    private static Connection connection = null;

    private DatabaseSqliteConnection() {}

    public static void initializeConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                initSchema(connection);
            }
        } catch (SQLException e) {
            throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
    }

    private static void initSchema(Connection connection) {
            try (Statement stmt = connection.createStatement()) {
                String schema = Files.readString(Path.of("src/main/java/infra/shared/config/db/sqlite/schema.sql"));
                String[] sqlStatements = schema.split(";");
                for (String sql : sqlStatements) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql.trim() + ";");
                    }
                }
            } catch (SQLException | IOException e) {
                throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
            }
    }
    
    public static Connection getConnection() {
        if (connection == null) {
	        throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

}