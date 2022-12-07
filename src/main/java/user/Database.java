package user;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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

    // Stores the current theme for logged in user.
    private static String currentTheme;

    /**
     * Sets the source to the serialized file for
     * where the user configurations can be found.
     *
     * @param filePath Path to the file.
     */
    public static void setConfigurationsSource(String filePath) {
        if (!configurationsSourceSet) {
            configurationsSource = filePath;
            configurationsSourceSet = true;

            boolean result = makeSource(filePath);
            if (!result) {
                userConfigurations = loadConfigurations();

                if (userConfigurations != null && userConfigurations.get("").get(0).equals("true")) {
                    loggedIn = true;
                } else {
                    loggedIn = false;
                }

            } else {
                userConfigurations = new HashMap<>();
                List<String> currentlyLoggedIn = new ArrayList<>(2);
                currentlyLoggedIn.add("None");
                currentlyLoggedIn.add("");
                userConfigurations.put("", currentlyLoggedIn);
            }
        }
    }

    /**
     * Sets the source to the serialized file for
     * where the user objects can be found.
     *
     * @param filePath Path to the file.
     */
    public static void setUsersSource(String filePath) {
        if (!usersSourceSet) {
            usersSource = filePath;
            usersSourceSet = true;

            boolean result = makeSource(filePath);

            if (!result) {
                users = loadUsers();

                if (loggedIn) {
                    String previousUser = userConfigurations.get("").get(1);
                    user = users.get(previousUser);
                    currentTheme = userConfigurations.get(previousUser.toLowerCase()).get(2);
                }

            } else {
                users = new HashMap<>();
            }
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
            List<String> configurations = new ArrayList<>(3);
            String salt = generateSalt();
            String hashed = hashPassword(password, salt.getBytes());
            currentTheme = "Light";
            configurations.add(hashed);
            configurations.add(salt);
            configurations.add(currentTheme);
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
     * @return The theme for this user.
     */
    public static String getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Set the preferred theme for the currently logged-in
     * user.
     *
     * @param newTheme The new theme to set for the user
     *                 currently logged-in.
     */
    public static void setCurrentTheme(String newTheme) {
        currentTheme = newTheme;
        userConfigurations.get(user.getUsername().toLowerCase()).set(2, newTheme);
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
        String result = hashPassword(password, salt.getBytes());
        loggedIn = configurations.get(0).equals(result);
        if (loggedIn) {
            user = users.get(username.toLowerCase());
            setLoginStatus("true");
            currentTheme = configurations.get(2);
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
     * Generate a salt which can be used for hashing algorithms.
     *
     * @return String version of the salt in Base 64
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a given password so that its secure if the file
     * is compromised. For extra security purposes.
     *
     * @param s The password
     * @param salt The salt being used to hash the password
     * @return String version of the hashed password in Base 64
     */
    private static String hashPassword(String s, byte[] salt) {
        KeySpec spec = new PBEKeySpec(s.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            return null;
        }

        return Base64.getEncoder().encodeToString(hash);
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
        serializeObject(configurationsSource, userConfigurations);
        serializeObject(usersSource, users);
    }

    /**
     * Serialize a given object to a file.
     *
     * @param source Path to file.
     * @param o Object to be serialized.
     */
    private static void serializeObject(String source, Object o) {
        FileOutputStream out = null;
        ObjectOutputStream writeObject = null;
        try {
            out = new FileOutputStream(source);
            writeObject = new ObjectOutputStream(out);
            writeObject.writeObject(o);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writeObject != null) {
                    writeObject.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load configurations for all the users who have an
     * account for this application.
     *
     * @return A Map containing a key as the username and a list
     * to store the configurations for a given user.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, List<String>> loadConfigurations() {
        FileInputStream in = null;
        ObjectInputStream readObject = null;
        try {
            in = new FileInputStream(configurationsSource);
            readObject = new ObjectInputStream(in);
            return (Map<String, List<String>>) readObject.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (readObject != null) {
                    readObject.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Load all the user objects for all the users that
     * have an account for this application.
     *
     * @return A Map where the username is the key and
     * the value is the corresponding User object for a given
     * user.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, User> loadUsers() {
        FileInputStream in = null;
        ObjectInputStream readObject = null;
        try {
            in = new FileInputStream(usersSource);
            readObject = new ObjectInputStream(in);
            return (Map<String, User>) readObject.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (readObject != null) {
                    readObject.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Clear data for the currently logged-in user.
     */
    public static void clearUserData() {
        user.clearAllAccounts();
        serializeObject(usersSource, users);
    }
}