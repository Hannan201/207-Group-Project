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
     * Add a new social media account for this user.
     *
     * @param newAccount The new social media account
     *                   to be added.
     */
    public void addNewAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    /**
     * Get the username for this user.
     *
     * @return Username for this user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Return the social media accounts for this
     * user.
     *
     * @return List of social media accounts.
     */
    public List<Account> getAccounts() {
        return this.accounts;
    }

    /**
     * Clear all the backup codes for this user.
     */
    public void clearAllAccounts() {
        for (Account account : this.accounts) {
            account.clearUserCodes();
        }
    }
}
