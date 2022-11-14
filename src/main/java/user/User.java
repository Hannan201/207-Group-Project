package user;

import java.util.List;

public class User {

    private String username;

    private String password;

    private List<Account> accounts;

    public User(String newUsername, String newPassword) {
        this.username = newUsername;
        this.password = newPassword;
    }

    public void deleteAccounts() {
        for (Account account : this.accounts) {
            account.clearUserCodes();
        }
    }
}
