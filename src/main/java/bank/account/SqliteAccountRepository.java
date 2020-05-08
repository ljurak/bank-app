package bank.account;

import bank.infrastructure.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqliteAccountRepository implements AccountRepository {

    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO card (number, pin) VALUES (?, ?)";

    private static final String SELECT_ACCOUNT_SQL = "SELECT * FROM card WHERE number = ?";

    private static final String CHECK_ACCOUNT_EXISTENCE_SQL = "SELECT EXISTS (SELECT * FROM card WHERE number = ?)";

    private static final String UPDATE_ACCOUNT_BALANCE = "UPDATE card SET balance = ? WHERE number = ?";

    private static final String DELETE_ACCOUNT_SQL = "DELETE FROM card WHERE number = ?";

    private final ConnectionProvider connectionProvider;

    public SqliteAccountRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void addAccount(Account account) {
        try (Connection connection = connectionProvider.connect()) {
            try {
                PreparedStatement ps = connection.prepareStatement(INSERT_ACCOUNT_SQL);
                ps.setString(1, account.getCardNumber());
                ps.setString(2, account.getPin());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public boolean containsAccount(String cardNumber) {
        try (Connection connection = connectionProvider.connect()) {
            try {
                PreparedStatement ps = connection.prepareStatement(CHECK_ACCOUNT_EXISTENCE_SQL);
                ps.setString(1, cardNumber);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 1) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Account findAccount(String cardNumber) {
        try (Connection connection = connectionProvider.connect()) {
            try {
                PreparedStatement ps = connection.prepareStatement(SELECT_ACCOUNT_SQL);
                ps.setString(1, cardNumber);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Account account = new Account(rs.getString(2), rs.getString(3));
                    account.setBalance(rs.getInt(4));
                    return account;
                }
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void updateAccount(Account account) {
        try (Connection connection = connectionProvider.connect()) {
            try {
                PreparedStatement ps = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE);
                ps.setInt(1, account.getBalance());
                ps.setString(2, account.getCardNumber());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public void deleteAccount(Account account) {
        try (Connection connection = connectionProvider.connect()) {
            try {
                PreparedStatement ps = connection.prepareStatement(DELETE_ACCOUNT_SQL);
                ps.setString(1, account.getCardNumber());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
