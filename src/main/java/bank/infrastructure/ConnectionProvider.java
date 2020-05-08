package bank.infrastructure;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
    Connection connect() throws SQLException;
}
