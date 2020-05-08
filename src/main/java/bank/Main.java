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
        System.out.println("2. Add income");
        System.out.println("3. Transfer money");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    private static void createAccount(AccountService accountService) {
        Account account = accountService.createAccount();
        System.out.println("Your card have been created.");
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
            processLoggedUser(accountService, account);
        } else {
            System.out.println("Wrong card number or PIN!" + System.lineSeparator());
        }
    }

    private static void processLoggedUser(AccountService accountService, Account account) {
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
                    depositMoney(accountService, account);
                    break;
                case 3:
                    transferMoney(accountService, account);
                    break;
                case 4:
                    closeAccount(accountService, account);
                    return;
                case 5:
                    System.out.println("You have successfully logged out!" + System.lineSeparator());
                    return;
                default:
                    System.out.println("Incorrect option. Try again." + System.lineSeparator());
                    break;
            }

            showAccountMenu();
        }

        System.out.println();
    }

    private static void depositMoney(AccountService accountService, Account account) {
        System.out.println("How much money would you like to deposit:");
        int money = Integer.parseInt(scanner.nextLine());
        System.out.println();

        accountService.depositMoney(account, money);
        System.out.println("Money has been deposited into your account." + System.lineSeparator());
    }

    private static void transferMoney(AccountService accountService, Account account) {
        System.out.println("Enter account number for money transfer:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter amount of money:");
        int money = Integer.parseInt(scanner.nextLine());
        System.out.println();

        if (money > account.getBalance()) {
            System.out.println("Insufficient money!" + System.lineSeparator());
            return;
        }

        if (account.getCardNumber().equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account!" + System.lineSeparator());
            return;
        }

        if (!accountService.checkCardNumberFormat(cardNumber)) {
            System.out.println("Probably you made mistake in card number. Please try again!" + System.lineSeparator());
            return;
        }

        Account transferAccount = accountService.findAccount(cardNumber);
        if (transferAccount == null) {
            System.out.println("Such a card does not exist!" + System.lineSeparator());
            return;
        }

        accountService.transferMoney(account, transferAccount, money);
        System.out.println("Money has been transferred." + System.lineSeparator());
    }

    private static void closeAccount(AccountService accountService, Account account) {
        accountService.closeAccount(account);
        System.out.println("Your account has been closed." + System.lineSeparator());
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
