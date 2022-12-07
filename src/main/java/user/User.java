package user;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for storing data
 * related to a user of this application.
 */

public class User implements java.io.Serializable {

    // The user's username.
    private String username;

    // List of social media accounts for this user.
    private List<Account> accounts;

    /**
     * Create a new user for this application with the
     * username and password passed in as a parameter.
     *
     * @param newUsername Username of this user.
     */
    public User(String newUsername) {
        this.username = newUsername;
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

    public Account getAccountByName(String name) {
        for (Account account : this.accounts) {
            if (account.getName().equals(name)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Clear all the backup codes for this user.
     */
    public void clearAllAccounts() {
        for (Account account : this.accounts) {
            account.clearUserCodes();
        }
        accounts.clear();
    }
}
