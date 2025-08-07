package bootstrap;
import infra.shared.config.db.sqlite.DatabaseSqliteConnection;

public class Main {
    public static void main(String[] args) {
        DatabaseSqliteConnection.initializeConnection();
    }
}
