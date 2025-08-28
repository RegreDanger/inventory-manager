package common.platform.config.db.sqlite;

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

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import bootstrap.Main;
import common.kernel.exceptions.api.InternalServerException;
import common.platform.config.db.sqlite.enums.SqlitePaths;

public class HikariSqlite {
    private static final Logger LOGGER = LoggerFactory.getLogger(HikariSqlite.class);

    private static HikariDataSource ds;

    public synchronized void initializeDatabase() {
        if (ds != null) return;
        LOGGER.info("Initializing SQLite database at: {}", SqlitePaths.SQLITE_DB_PATH.getPath());
        try {
            Path path = Paths.get(SqlitePaths.SQLITE_DB_PATH.getPath());
            
            if (path != null && Files.notExists(path)) {
                Files.createDirectories(path);
            }

            HikariConfig config = new HikariConfig(SqlitePaths.SQLITE_PROPERTIES_PATH.getPath());
            ds = new HikariDataSource(config);

            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement()) {
                LOGGER.info("Setting SQLite database to WAL mode and NORMAL synchronous mode.");
                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.execute("PRAGMA synchronous=NORMAL;");
                LOGGER.info("initializing SQLite schema.");
                initializeSchema(conn);
            }

        } catch (IOException | SQLException e) {
            throw new InternalServerException("Failed to initialize SQLite database", e);
        }
    }

    private void initializeSchema(Connection conn) {
        try (InputStream inputStream = Main.class.getResourceAsStream(ds.getSchema())) {
            if (inputStream == null) {
                throw new InternalServerException(new FileNotFoundException("Schema not found: " + ds.getSchema()));
            }
            LOGGER.info("Reading schema script: {}", ds.getSchema());
            try (Reader reader = new InputStreamReader(inputStream)) {
                ScriptRunner scriptRunner = new ScriptRunner(conn);
                scriptRunner.setStopOnError(true);
                scriptRunner.setDelimiter(";");
                LOGGER.info("Running schema script: {}", ds.getSchema());
                scriptRunner.runScript(reader);
            }
        } catch (IOException e) {
            throw new InternalServerException("Failed to read schema script for SQLite database", e);
        }
    }

    public Connection getConnection() {
        try {
            return getDs().getConnection();
        } catch (SQLException e) {
            throw new InternalServerException("Failed to get connection from SQLite database", e);
        }
    }

    public DataSource getDs() {
        if (ds == null) {
            throw new InternalServerException(new NullPointerException("Datasource is null"));
        }
        return ds;
    }

    public void shutdown() {
        if (ds != null) {
            ds.close();
        }
    }
}
