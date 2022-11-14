package user;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for storing data
 * related to a user of this application.
 */

public class User {

    // The user's username.
    private String username;

    // The user's password.
    private String password;

    // List of social media accounts for this user.
    private List<Account> accounts;

    /**
     * Create a new user for this application with the
     * username and password passed in as a parameter.
     *
     * @param newUsername Username of this user.
     * @param newPassword Password for this user.
     */
    public User(String newUsername, String newPassword) {
        this.username = newUsername;
        this.password = newPassword;
        accounts = new ArrayList<>();
    }

    /**
     * Clear all the backup codes for this user.
     */
    public void deleteAccounts() {
        for (Account account : this.accounts) {
            account.clearUserCodes();
        }
    }
}
