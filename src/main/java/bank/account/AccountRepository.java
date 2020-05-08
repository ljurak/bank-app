package bank.account;

public interface  AccountRepository {
    void addAccount(Account account);
    boolean containsAccount(String cardNumber);
    Account findAccount(String cardNumber);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
