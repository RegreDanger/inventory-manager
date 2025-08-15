package infra.shared.config.db.sqlite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import kernel.impl.exceptions.InternalServerException;
import kernel.utils.LoggingUtils;
import kernel.utils.enums.ErrorCode;
import infra.shared.config.db.sqlite.enums.SqlitePaths;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import bootstrap.Main;

import org.apache.ibatis.jdbc.ScriptRunner;

public class HikariSqlite {

    private static HikariDataSource ds;

    private HikariSqlite() {}

    public static synchronized void initializeDatabase() {
        if (ds != null) return;
        LoggingUtils.getLogger(HikariSqlite.class).info("Initializing SQLite database at: {}", SqlitePaths.SQLITE_DB_PATH.getPath());
        try {
            Path path = Paths.get(SqlitePaths.SQLITE_DB_PATH.getPath());
            
            if (path != null && Files.notExists(path)) {
                Files.createDirectories(path);
            }

            HikariConfig config = new HikariConfig(SqlitePaths.SQLITE_PROPERTIES_PATH.getPath());
            ds = new HikariDataSource(config);

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                LoggingUtils.getLogger(HikariSqlite.class).info("Setting SQLite database to WAL mode and NORMAL synchronous mode.");
                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.execute("PRAGMA synchronous=NORMAL;");
                LoggingUtils.getLogger(HikariSqlite.class).info("initializing SQLite schema.");
                initializeSchema(conn);
            }

        } catch (IOException | SQLException e) {
            throw new InternalServerException("Failed to initialize SQLite database",
                    ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    private static void initializeSchema(Connection conn) {
        try (InputStream inputStream = Main.class.getResourceAsStream(ds.getSchema())) {
            if (inputStream == null) {
                Throwable cause = new FileNotFoundException("Schema not found: " + ds.getSchema());
                throw new InternalServerException("Schema script not found: " + ds.getSchema(),
                        ErrorCode.INTERNAL_SERVER_ERROR, cause);
            }
            LoggingUtils.getLogger(HikariSqlite.class).info("Reading schema script: {}", ds.getSchema());
            try (Reader reader = new InputStreamReader(inputStream)) {
                ScriptRunner scriptRunner = new ScriptRunner(conn);
                scriptRunner.setStopOnError(true);
                scriptRunner.setDelimiter(";");
                LoggingUtils.getLogger(HikariSqlite.class).info("Running schema script: {}", ds.getSchema());
                scriptRunner.runScript(reader);
            }
        } catch (IOException e) {
            throw new InternalServerException("Failed to read schema script for SQLite database",
                    ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public static Connection getConnection() {
        if (ds == null) {
            Throwable cause = new IllegalStateException("Datasource is null");
            throw new InternalServerException("Database has not been initialized.",
                    ErrorCode.INTERNAL_SERVER_ERROR, cause);
        }
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new InternalServerException("Failed to get connection from SQLite database",
                    ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public static DataSource getDs() {
        if (ds == null) {
            Throwable cause = new IllegalStateException("Datasource is null");
            throw new InternalServerException("Database has not been initialized.",
                    ErrorCode.INTERNAL_SERVER_ERROR, cause);
        }
        return ds;
    }

    public static void shutdown() {
        if (ds != null) {
            ds.close();
        }
    }
}
