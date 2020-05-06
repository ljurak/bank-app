package bank;

import java.util.Random;

public class AccountService {

    private AccountRepository accountRepo = new AccountRepository();

    private Random random = new Random();

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

    public Account logIntoAccount(String cardNumber, String pin) {
        Account account = accountRepo.findAccount(cardNumber);

        if (account != null) {
            if (account.getPin().equals(pin)) {
                return account;
            }
        }

        return null;
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("400000");
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
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
