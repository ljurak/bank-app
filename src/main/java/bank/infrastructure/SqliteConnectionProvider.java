package bank.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnectionProvider implements ConnectionProvider {

    private final String url;

    public SqliteConnectionProvider(String url) {
        this.url = url;
    }

    @Override
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
