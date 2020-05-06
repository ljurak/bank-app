package bank;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getCardNumber(), account);
    }

    public boolean containsAccount(String cardNumber) {
        return accounts.containsKey(cardNumber);
    }

    public Account findAccount(String cardNumber) {
        return accounts.get(cardNumber);
    }
}
