package bank.account;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();

    @Override
    public void addAccount(Account account) {
        accounts.put(account.getCardNumber(), account);
    }

    @Override
    public boolean containsAccount(String cardNumber) {
        return accounts.containsKey(cardNumber);
    }

    @Override
    public Account findAccount(String cardNumber) {
        return accounts.get(cardNumber);
    }

    @Override
    public void updateAccount(Account account) {
        accounts.put(account.getCardNumber(), account);
    }

    @Override
    public void deleteAccount(Account account) {
        accounts.remove(account.getCardNumber());
    }
}