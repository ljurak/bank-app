package bank;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AccountService accountService = new AccountService();

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
}
