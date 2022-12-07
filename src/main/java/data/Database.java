package data;

import user.User;

import java.io.*;
import java.util.*;

/**
 * This class is responsible for loading and
 * saving data for users in this application.
 */

public class Database {

    // To check if a user's already logged in.
    private static boolean loggedIn;

    // Store current user to load or save data.
    private static User user;

    // Source for the usernames, passwords, and
    // any configurations.
    private static String configurationsSource;

    // To only allow one source to be set
    // for the user configurations
    private static boolean configurationsSourceSet;

    // Source for the user objects in this
    // application.
    private static String usersSource;

    // To only allow one source to be
    // set for the user objects in this
    // application.
    private static boolean usersSourceSet;

    // This stores a user's username, hashed version of their
    // password, the salt for the hash and their current
    // theme.
    private static Map<String, List<String>> userConfigurations;

    // This stores the corresponding User object for every
    // username in this application.
    private static Map<String, User> users;

    // To save user data.
    private static DataSaver saveUsers;

    // To save user configurations.
    private static DataSaver saveConfigurations;

    // To hash passwords which the user enters.
    private static PasswordHasher passwordHasher;

    /**
     * Sets the source to the serialized file for
     * where the user configurations can be found.
     *
     * @param filePath Path to the file.
     */
    @SuppressWarnings("unchecked")
    public static void setConfigurationsSource(String filePath) {
        if (!configurationsSourceSet) {
            configurationsSource = filePath;
            DataLoader loadConfigurations = new DataLoader(filePath);
            passwordHasher = new PasswordHasher();
            configurationsSourceSet = true;

            boolean result = makeSource(filePath);
            if (!result) {
                userConfigurations = (Map<String, List<String>>) loadConfigurations.load();

                loggedIn = userConfigurations != null && userConfigurations.get("").get(0).equals("true");

            } else {
                userConfigurations = new HashMap<>();
                List<String> currentlyLoggedIn = new ArrayList<>(2);
                currentlyLoggedIn.add("None");
                currentlyLoggedIn.add("");
                userConfigurations.put("", currentlyLoggedIn);
            }

            saveConfigurations = new DataSaver(filePath, userConfigurations);
        }
    }

    /**
     * Sets the source to the serialized file for
     * where the user objects can be found.
     *
     * @param filePath Path to the file.
     */
    @SuppressWarnings("unchecked")
    public static void setUsersSource(String filePath) {
        if (!usersSourceSet) {
            usersSource = filePath;
            usersSourceSet = true;
            DataLoader loadUsers = new DataLoader(filePath);

            boolean result = makeSource(filePath);

            if (!result) {
                users = (Map<String, User>) loadUsers.load();

                if (loggedIn) {
                    String previousUser = userConfigurations.get("").get(1);
                    user = users.get(previousUser);
                }

            } else {
                users = new HashMap<>();
            }

            saveUsers = new DataSaver(filePath, users);
        }
    }

    /**
     * Check to see if a specific file is made. If the file is
     * not made, a new file will be created.
     *
     * @param source Path to the file.
     * @return Return false if the file already exists, true
     * otherwise.
     */
    private static boolean makeSource(String source) {
        File file = new File(source);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("File does not exist and cannot be made.");
                }
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    /**
     * Register a new user for this application.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     */
    public static void registerUser(String username, String password) {
        if (usersSourceSet && configurationsSourceSet && !loggedIn) {
            List<String> configurations = new ArrayList<>(2);
            String salt = passwordHasher.generateSalt();
            String hashed = passwordHasher.hashPassword(password, salt.getBytes());
            configurations.add(hashed);
            configurations.add(salt);
            loggedIn = true;
            user = new User(username);
            setLoginStatus("true");
            userConfigurations.put(username.toLowerCase(), configurations);
            users.put(username.toLowerCase(), user);
        }
    }

    /**
     * Check whether a user is logged-in to this application.
     *
     * @return True if logged in, false otherwise.
     */
    public static boolean getLoginStatus() {
        return loggedIn;
    }

    /**
     * Set the login status for this current user. To remember
     * whether if this user logged out before closing the
     * application.
     *
     * @param status The new status.
     */
    private static void setLoginStatus(String status) {
        userConfigurations.get("").set(0, status);
        if (status.equals("true")) {
            userConfigurations.get("").set(1, user.getUsername().toLowerCase());
        } else if (status.equals("false") || status.equals("none")) {
            userConfigurations.get("").set(1, "");
        }
    }

    /**
     * Get the preferred theme for the currently logged-in user.
     *
     * @return The theme for the user currently logged-in.
     */
    public static String getCurrentTheme() {
        return user.getCurrentTheme();
    }

    /**
     * Set the preferred theme for the currently logged-in
     * user.
     *
     * @param newTheme The new theme to set for the user
     *                 currently logged-in.
     */
    public static void setCurrentTheme(String newTheme) {
        user.setCurrentTheme(newTheme);
    }


    /**
     * Check if a username is already taken.
     *
     * @param username The username to check for.
     * @return True if username is taken, false otherwise.s
     */
    public static boolean checkUsername(String username) {
        return users.containsKey(username.toLowerCase());
    }

    /**
     * Authenticate an existing user into this application
     * to ensure their username exists and their password is
     * correct.
     *
     * @param username The username for the user trying to log-in.
     * @param password The password for the user trying to log-in.
     * @return True if the user was logged-in, false otherwise.
     */
    public static boolean authenticateUser(String username, String password) {
        if (!checkUsername(username.toLowerCase())) {
            loggedIn = false;
            return false;
        }

        List<String> configurations = userConfigurations.get(username.toLowerCase());
        String salt = configurations.get(1);
        loggedIn = passwordHasher.verifyPassword(configurations.get(0), password, salt);
        if (loggedIn) {
            user = users.get(username.toLowerCase());
            setLoginStatus("true");
        } else {
            user = null;
        }

        return loggedIn;
    }

    /**
     * Get the current logged-in user object.
     *
     * @return Current logged in user.
     */
    public static User getUser() {
        if (loggedIn) {
            return user;
        }
        return null;
    }





    /**
     * Log user out of this application.
     */
    public static void logUserOut() {
        loggedIn = false;
        user = null;
        setLoginStatus("false");
        saveUserData();
    }

    /**
     * Save user data for the currently logged-in user.
     */
    public static void saveUserData() {
        saveUsers.save();
        saveConfigurations.save();
    }

    /**
     * Clear data for the currently logged-in user.
     */
    public static void clearUserData() {
        user.clearAllAccounts();
        saveUsers.save();
    }
}