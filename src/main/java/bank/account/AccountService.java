package bank.account;

import java.util.Arrays;
import java.util.Random;

public class AccountService {

    private final AccountRepository accountRepo;

    private Random random = new Random();

    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Account createAccount() {
        Account account = null;
        boolean accountCreated = false;

        while (!accountCreated) {
            String cardNumber = generateCardNumber();

            if (!accountRepo.containsAccount(cardNumber)) {
                String pin = generatePIN();
                account = new Account(cardNumber, pin);
                accountRepo.addAccount(account);
                accountCreated = true;
            }
        }

        return account;
    }

    public Account findAccount(String cardNumber) {
        return accountRepo.findAccount(cardNumber);
    }

    public Account logIntoAccount(String cardNumber, String pin) {
        Account account = accountRepo.findAccount(cardNumber);

        if (account != null) {
            if (account.getPin().equals(pin)) {
                return account;
            }
        }

        return null;
    }

    public void depositMoney(Account account, int money) {
        account.setBalance(account.getBalance() + money);
        accountRepo.updateAccount(account);
    }

    public void transferMoney(Account from, Account to, int money) {
        if (money > from.getBalance()) {
            return;
        }

        from.setBalance(from.getBalance() - money);
        to.setBalance(to.getBalance() + money);

        accountRepo.updateAccount(from);
        accountRepo.updateAccount(to);
    }

    public void closeAccount(Account account) {
        accountRepo.deleteAccount(account);
    }

    public boolean checkCardNumberFormat(String cardNumber) {
        if (!cardNumber.matches("\\d{16}")) {
            return false;
        }

        int[] digits = Arrays.stream(cardNumber.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        int sum = 0;

        for (int i = 0; i < 15; i++) {
            int number = digits[i];

            if (i % 2 == 0) {
                number *= 2;
            }

            if (number > 9) {
                number -= 9;
            }

            sum += number;
        }

        sum += digits[15];

        return sum % 10 == 0;
    }

    private String generateCardNumber() {
        int[] digits = new int[16];
        digits[0] = 4;

        for (int i = 6; i < 15; i++) {
            digits[i] = random.nextInt(10);
        }

        int sum = 0;

        for (int i = 0; i < 15; i++) {
            int number = digits[i];

            if (i % 2 == 0) {
                number *= 2;
            }

            if (number > 9) {
                number -= 9;
            }

            sum += number;
        }

        if (sum % 10 != 0) {
            digits[15] = 10 - sum % 10;
        }

        StringBuilder cardNumber = new StringBuilder();
        for (int digit : digits) {
            cardNumber.append(digit);
        }

        return cardNumber.toString();
    }

    private String generatePIN() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }
        return pin.toString();
    }
}
