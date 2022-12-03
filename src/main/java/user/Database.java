package user;

import java.util.Set;

/**
 * This class is responsible for loading and
 * saving data for users in this application.
 */

public class Database {

    // To check if a user's already logged in.
    private static boolean loggedIn;

    // To prevent duplicate usernames.
    private static Set<String> usernames;

    // Store current user to load or save data.
    private static User user;

    // Source for the usernames, passwords, and
    // any configurations.
    private static String userSource;

    // To only allow onc source to be set
    // for usernames and passwords.
    private static boolean userSourceSet;

    // Source for the social media accounts
    // and backup codes for that account.
    private static String accountsSource;

    // To only allow one source to be
    // set for the backup codes and
    // accounts.
    private static boolean accountSourceSet;

    /**
     * Sets the source to the CSV file where
     * the usernames and passwords are stored.
     *
     * @param filePath Path to the file.
     */
    public static void setUserSource(String filePath) {
        if (!userSourceSet) {
            userSource = filePath;
        }
    }

    /**
     * Set the source to the file where
     * the social media accounts and
     * backup codes can be found.
     *
     * @param filePath Path to the file.
     */
    public static void setAccountsSource(String filePath) {
        if (!accountSourceSet) {
            accountsSource = filePath;
        }
    }

    public static void registerUser(String username, String password) {
        if (accountSourceSet && userSourceSet && !loggedIn) {

        }
    }
}
