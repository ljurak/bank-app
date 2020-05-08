package bank;

import bank.account.Account;
import bank.account.AccountRepository;
import bank.account.AccountService;
import bank.account.SqliteAccountRepository;
import bank.infrastructure.ConnectionProvider;
import bank.infrastructure.SqliteConnectionProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String CREATE_TABLE_SQL = "CREATE TABLE card (id INTEGER PRIMARY KEY, number TEXT NOT NULL, pin TEXT NOT NULL, balance INTEGER DEFAULT 0)";

    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("-filename")) {
            throw new IllegalArgumentException("Missing -filename argument (e.g. java -jar bank-app.jar -filename data.db)");
        }

        String filename = args[1];
        File file = new File(filename);

        ConnectionProvider connectionProvider = new SqliteConnectionProvider("jdbc:sqlite:" + filename);

        try {
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    createDatabase(connectionProvider);
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot create file: " + filename);
            System.exit(0);
        }

        AccountRepository accountRepo = new SqliteAccountRepository(connectionProvider);
        AccountService accountService = new AccountService(accountRepo);

        showMenu();
        int option;
        while ((option = Integer.parseInt(scanner.nextLine())) != 0) {
            System.out.println();

            switch (option) {
                case 1:
                    createAccount(accountService);
                    break;
                case 2:
                    logIntoAccount(accountService);
                    break;
                default:
                    System.out.println("Incorrect option. Try again." + System.lineSeparator());
                    break;
            }

            showMenu();
        }

        System.out.println(System.lineSeparator() + "Bye!");
    }

    private static void showMenu() {
        System.out.println("1. Create account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    private static void showAccountMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Log out");
        System.out.println("0. Exit");
    }

    private static void createAccount(AccountService accountService) {
        Account account = accountService.createAccount();
        System.out.println("Your card have been created");
        System.out.println("Your card number: " + account.getCardNumber());
        System.out.println("Your card PIN: " + account.getPin());
        System.out.println();
    }

    private static void logIntoAccount(AccountService accountService) {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();

        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        System.out.println();

        Account account = accountService.logIntoAccount(cardNumber, pin);

        if (account != null) {
            System.out.println("You have successfully logged in!" + System.lineSeparator());

            showAccountMenu();
            int option;
            while ((option = Integer.parseInt(scanner.nextLine())) != 0) {
                System.out.println();

                switch (option) {
                    case 1:
                        System.out.println("Balance: " + account.getBalance() + System.lineSeparator());
                        break;
                    case 2:
                        System.out.println("You have successfully logged out!" + System.lineSeparator());
                        return;
                    default:
                        System.out.println("Incorrect option. Try again." + System.lineSeparator());
                        break;
                }

                showAccountMenu();
            }

            System.out.println();
        } else {
            System.out.println("Wrong card number or PIN!" + System.lineSeparator());
        }
    }

    private static void createDatabase(ConnectionProvider connectionProvider) {
        try (Connection connection = connectionProvider.connect()) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(CREATE_TABLE_SQL);
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
