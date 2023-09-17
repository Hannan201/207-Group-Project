package cypher.enforcers.data.database;

import cypher.enforcers.data.*;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.security.Token;
import cypher.enforcers.data.security.TokenGenerator;
import cypher.enforcers.models.Account;
import cypher.enforcers.code.Code;
import cypher.enforcers.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.utilities.sqliteutilities.SQLiteHelper;
import cypher.enforcers.utilities.sqliteutilities.argumentsetters.IntegerSetter;
import cypher.enforcers.utilities.sqliteutilities.argumentsetters.StringSetter;
import cypher.enforcers.utilities.sqliteutilities.retrievers.CodeRetriever;
import cypher.enforcers.utilities.sqliteutilities.retrievers.IntegerRetriever;
import cypher.enforcers.utilities.sqliteutilities.retrievers.ListRetriever;
import cypher.enforcers.utilities.sqliteutilities.retrievers.StringRetriever;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.*;

/**
 * This class is responsible for loading and
 * saving data for users in this application.
 */

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    // To hash passwords which the user enters.
    private static PasswordHasher passwordHasher;

    // To generate a token for the user.
    private static TokenGenerator generator;

    // To help interface with the database.
    private static SQLiteHelper sqLiteHelper;

    /*
     These methods are for connecting and
     configuring the SQLite database.
     */

    /**
     * Perform a safety check before a change to the database is made.
     *
     * @param token Token for check for authentication failure.
     * @return True if there is a failure, false otherwise.
     */
    public static boolean safetyChecks(Token token) {
        return !sqLiteHelper.connectionFailure() && validToken(token);
    }

    /**
     * The SQLite file this database needs to connect to.
     *
     * @param source Name of the database file.
     */
    public static void setConnectionSource(String source) {
        if (source.isBlank() || !source.endsWith(".db")) {
            logger.warn("Invalid path to database, connection failed. Aborting request.");
            return;
        }

        TokenManager.initializeStorage();
        passwordHasher = new PasswordHasher();
        generator = new TokenGenerator(21);
        sqLiteHelper = new SQLiteHelper(source);
    }

    /**
     * Disconnect from the database.
     */
    public static void disconnect() {
        sqLiteHelper.disconnect();
    }

    /*
     These methods are for signing-up, signing-in, and signing-out.
     */

    /**
     * Check if a token matches what's currently
     * stored. This is to prevent access from unknown users.
     *
     * @param token The token.
     * @return True if the token does match, false otherwise.
     */
    private static boolean validToken(Token token) {
        String[] result = Objects.requireNonNull(TokenManager.parseToken());
        if (result.length != 2) {
            logger.warn("Authentication failure. Access denied.");
            return false;
        }

        CustomToken casted = (CustomToken) token;

        if (!result[0].equals(casted.key) || Integer.parseInt(result[1]) != casted.ID) {
            logger.warn("Authentication failure. Access denied.");
            return false;
        }

        return true;
    }

    /**
     * Check if a username is already taken.
     *
     * @param username The username to check for.
     * @return True if username is taken, false otherwise.
     */
    public static boolean checkUsername(String username) {
        if (username.isEmpty() || username.isBlank()) {
            logger.warn("Username cannot be empty.");
            return true;
        }

        if (!sqLiteHelper.connectionFailure()) {
            logger.trace("Attempt to check if username {} is taken.", username);

            Boolean bool = sqLiteHelper.executeSelect(
                """
                    SELECT (COUNT(*) > 0) AS 'contains' FROM users
                    WHERE username = ?
                    """,
                    new StringSetter(username.toLowerCase()),
                    resultSet -> {
                        try {
                            return resultSet.getBoolean("contains");
                        } catch (SQLException e) {
                            logger.warn("Failed to retrieve key contains. Cause: ", e);
                            e.printStackTrace();
                        }

                        return null;
                    }
            );

            if (bool == null) {
                logger.warn("Failed to check if username {} was taken. Returning true.", username);
                return true;
            }

            return bool;
        }

        return true;
    }

    /*
     This class is responsible for loading and saving the current token
     to protect the database from unauthorised access.
     */
    private static class TokenManager {

        private static final Logger tokenLogger = LoggerFactory.getLogger(TokenManager.class);

        // Connection to the keystore object.
        private static KeyStore storage;

        // Password for the keystore.
        private static char[] password;

        // Password for the keystore, as a protection parameter.
        private static KeyStore.ProtectionParameter keyStorePassword;

        // Used to generate secrets.
        private static SecretKeyFactory factory;

        static {
            try {
                factory = SecretKeyFactory.getInstance("PBE");
            } catch (NoSuchAlgorithmException e) {
                logger.warn("Failed to initialize factory. Cause: ", e);
                e.printStackTrace();
            }
        }

        /**
         * Load the token into storage, if there's any present.
         */
        private static void initializeStorage() {
            String[] result = Objects.requireNonNull(parseToken());

            if (result.length != 2) {
                tokenLogger.warn("No token found, result must be of length 2, but is in fact of size {}. Aborting.", result.length);
                return;
            }

            tokenLogger.debug("Token found.");
            Storage.setToken(
                    new CustomToken(
                            result[0],
                            Integer.parseInt(result[1])
                    )
            );
        }

        /**
         * Load the properties file.
         */
        private static void initializeProperties() {
            InputStream applicationFile = null;
            try {
                applicationFile = Utilities.loadFileByInputStream("application.properties");

                Properties applicationProperties = new Properties();
                applicationProperties.load(applicationFile);
                password = Objects.requireNonNull(
                        applicationProperties.getProperty("password")
                ).toCharArray();
                keyStorePassword = new KeyStore.PasswordProtection(password);
            } catch (IOException e) {
                tokenLogger.warn("Failed to read application.properties. Cause: ", e);
                e.printStackTrace();
            } finally {
                try {
                    if (applicationFile != null) {
                        applicationFile.close();
                    }
                } catch (IOException e2) {
                    tokenLogger.warn("Failed to close input stream for application.properties. Cause: ", e2);
                    e2.printStackTrace();
                }
            }
        }

        /**
         * Initialize the key store file.
         */
        private static void initializeKeyStore() {
            InputStream stream = null;
            try {
                stream = Utilities.loadFileByInputStream("token.pfx");
                storage = KeyStore.getInstance(KeyStore.getDefaultType());
                storage.load(stream, password);
            } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
                tokenLogger.warn("Failed to extract token. Cause: ", e);
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e2) {
                    tokenLogger.warn("Failed to close input stream for token.pfx. Cause: ", e2);
                    e2.printStackTrace();
                }
            }
        }

        /**
         * Update the current token to a new token.
         *
         * @param newToken The new token.
         */
        private static void updateToken(String newToken) {
            if (password == null) {
                initializeProperties();
            }

            if (storage == null) {
                initializeKeyStore();
            }

            FileOutputStream storeOutput = null;
            try {
                SecretKey secureKey = factory.generateSecret(
                        new PBEKeySpec(newToken.toCharArray())
                );

                KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secureKey);
                storage.setEntry("token", secret, keyStorePassword);
                URL url = Utilities.loadFileByURL("token.pfx");
                storeOutput = new FileOutputStream(url.getPath());
                storage.store(storeOutput, password);
            } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | KeyStoreException |
                     CertificateException e) {
                tokenLogger.warn("Failed to update token for token.pfx. Cause: ", e);
                e.printStackTrace();
            } finally {
                try {
                    if (storeOutput != null) {
                        storeOutput.close();
                    }
                } catch (IOException e2) {
                    tokenLogger.warn("Failed to close output stream for token.pfx. Cause: ", e2);
                    e2.printStackTrace();
                }
            }
        }

        /**
         * Return the current token as an array.
         *
         * @return Array of size 2 if there's a token present with
         * the first index being the token and the second index being
         * the id it belongs to, otherwise null.
         */
        public static String[] parseToken() {
            if (password == null) {
                initializeProperties();
            }

            if (storage == null) {
                initializeKeyStore();
            }

            try {
                KeyStore.SecretKeyEntry key = (KeyStore.SecretKeyEntry) storage.getEntry("token", keyStorePassword);
                PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(
                        key.getSecretKey(),
                        PBEKeySpec.class
                );

                return new String(keySpec.getPassword()).split(",");
            } catch (InvalidKeySpecException | UnrecoverableEntryException | KeyStoreException | NoSuchAlgorithmException e) {
                tokenLogger.warn("Unable to parse token, returning null. Cause: ", e);
                e.printStackTrace();
            }

            return null;
        }
    }

    /*
     This record acts as a token which stores the ID of which user
     it belongs to in the database. Made private to prevent outside
     access.
     */
    private record CustomToken(String key, int ID) implements Token {

    }

    /**
     * Register a new user for this application.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     */
    public static Token registerUser(String username, String password) {
        if (!sqLiteHelper.connectionFailure()) {
            logger.trace("Attempt to register a user with username {}.", username);

            String salt = passwordHasher.generateSalt();
            String hashed = salt + passwordHasher.hashPassword(password + salt, salt.getBytes());

            Integer i = sqLiteHelper.executeUpdateWithKey(
                """
                    INSERT INTO users
                    (username, password)
                    VALUES
                    (?, ?)
                    """,
                    new StringSetter(username.toLowerCase()),
                    new StringSetter(hashed)
            );

            if (i != null) {
                String token = generator.nextString();
                TokenManager.updateToken(token + "," + i);

                logger.trace("User registered.");
                return new CustomToken(token, i);
            }

            logger.warn("Failed to register user with username {}.", username);
            return null;
        }

        return null;
    }

    /**
     * Authenticate an existing user into this application
     * to ensure their username exists and their password is
     * correct.
     *
     * @param username The username for the user trying to log-in.
     * @param password The password for the user trying to log-in.
     * @return Token if the user was logged-in, null otherwise.
     */
    public static Token authenticateUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            logger.warn("Password and username cannot be empty. Returning null.");
            return null;
        }

        if (!checkUsername(username)) {
            logger.warn("username already taken. Returning null.");
            return null;
        }

        if (sqLiteHelper.connectionFailure()) {
            return null;
        }

        logger.trace("Attempt to authenticate user with username {}.", username);

        Token token = sqLiteHelper.executeSelect(
            """
                SELECT id, username, password FROM users
                WHERE username = ?
                """,
                new StringSetter(username.toLowerCase()),
                resultSet -> {
                    try {
                        String hashedPassword = resultSet.getString("password");

                        String salt = hashedPassword.substring(0, 24);
                        String expectedPassword = hashedPassword.substring(24);
                        if (passwordHasher.verifyPassword(expectedPassword, password + salt, salt)) {
                            String stringToken = generator.nextString();
                            int id = resultSet.getInt("id");

                            TokenManager.updateToken(stringToken + "," + id);
                            CustomToken customToken = new CustomToken(stringToken, id);

                            updateLoginStatus(customToken, true);

                            return customToken;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
        );

        if (token == null) {
            logger.warn("Failed to authenticate user with username {}.", username);
            return null;
        }

        logger.trace("User authenticated.");
        return token;
    }

    /**
     * Log out a user from this application.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static void logUserOut(Token token) {
        logger.trace("Attempt to log out user with ID {} of this application.", ((CustomToken) token).ID);
        updateLoginStatus(token, false);
        TokenManager.updateToken("null");
    }

    /*
     These methods are for interacting with the user's information such
     as accounts, codes, login status, and themes.
     */

    /**
     * Get a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @return User object if logged in, null otherwise.
     */
    public static User getUser(Token token) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to get user with ID {}.", id);

            User user = sqLiteHelper.executeSelect(
                """
                    SELECT * FROM users
                    WHERE id = ?
                    """,
                    new IntegerSetter(id),
                    resultSet -> {
                        try {
                            int userID = resultSet.getInt("id");
                            String name = resultSet.getString("username");
                            String theme = getThemeByID(resultSet.getInt("theme_id"));
                            return new User(userID, name, theme);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
            );

            if (user == null) {
                logger.warn("No user found with ID {}. Returning null,", id);
                return null;
            }

            logger.trace("User found.");
            return user;
        }

        return null;
    }

    /**
     * Get all accounts for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static List<Account> getAccounts(Token token) {
        List<Account> accounts = new ArrayList<>();

        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to get all accounts for user with ID {}.", id);

            List<Account> result = sqLiteHelper.executeSelect(
                """
                    SELECT id, name, type FROM accounts
                    WHERE user_id = ?
                    """,
                    new IntegerSetter(id),
                    new ListRetriever<>(accounts, resultSet -> {
                        try {
                            int accountID = resultSet.getInt("id");
                            String name = resultSet.getString("name");
                            String type = resultSet.getString("type");
                            return new Account(accountID, name, type);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        return null;
                    })
            );

            if (result.size() == 0) {
                logger.warn("No accounts found for user with ID {}.", id);
            } else {
                logger.trace("Accounts retrieved.");
            }
        }

        return accounts;
    }

    /**
     * Get an account for a user given the ID.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the account.
     */
    public static Account getAccount(Token token, int accountID) {
        int id = ((CustomToken) token).ID;
        logger.trace("Attempting to get account with ID {} for user with ID {}.", accountID, id);
        List<Account> accounts = Database.getAccounts(token);

        for (Account temp : accounts) {
            if (temp.getID() == accountID) {
                logger.trace("Account found.");
                return temp;
            }
        }

        logger.warn("Failed to get account with ID {} for user with ID {}. Returning null.", accountID, id);
        return null;
    }

    /**
     * Get an account for a user give the name.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param name Name of the account.
     * @return Account if found, otherwise null.
     */
    public static Account getAccountByName(Token token, String name) {
        int id = ((CustomToken) token).ID;
        logger.trace("Attempting to get account with name {} for user with ID {}.", name, id);

        List<Account> accounts = Database.getAccounts(token);

        for (Account temp : accounts) {
            if (temp.getName().equals(name)) {
                logger.trace("Account found.");
                return temp;
            }
        }

        logger.warn("Failed to get account with name {} for user with ID {}. Returning null.", name, id);
        return null;
    }

    /**
     * Add a new account for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param name The name of the account to be added.
     * @param type The type of the account to be added.
     * @return ID of the new account.
     */
    public static int addAccount(Token token, String name, String type) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to add account with name {} and social media type {} for user with ID {}.", name, type, id);

            Integer i = sqLiteHelper.executeUpdateWithKey(
                """
                    INSERT INTO accounts
                    (user_id, name, type)
                    VALUES
                    (?, ?, ?)
                    """,
                    new IntegerSetter(((CustomToken) token).ID),
                    new StringSetter(name),
                    new StringSetter(type)
            );

            if (i == null) {
                logger.warn("Failed to add account with name {} and social media type {} for user with ID {}. Returning -1.", name, type, id);
                return -1;
            }

            logger.trace("Account added with ID {}.", i);
            return i;
        }

        return -1;
    }

    /**
     * Remove an account for a user give the ID.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the account.
     */
    public static void removeAccount(Token token, int accountID) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to delete account for user with ID {} and with account ID {}.", id, accountID);

            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM accounts
                    WHERE id = ?
                    """,
                    new IntegerSetter(accountID)
            );

            logger.trace("Update complete.");
        }
    }

    /**
     * Clear all accounts for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static void clearAllAccounts(Token token) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to delete all accounts for user with ID {}.", id);

            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM accounts
                    WHERE user_id = ?
                    """,
                    new IntegerSetter(((CustomToken) token).ID)
            );

            logger.trace("Update complete.");
        }
    }

    /**
     * Get all the codes for an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the account.
     * @return List of codes contained within the account.
     */
    public static List<Code> getCodes(Token token, int accountID) {
        List<Code> codes = new ArrayList<>();

        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to retrieve all codes for user with ID {} and with account ID {}.", id, accountID);

            List<Code> result = sqLiteHelper.executeSelect(
                """
                    SELECT id, code FROM codes
                    WHERE account_id = ?
                    """,
                    new IntegerSetter(accountID),
                    new ListRetriever<>(codes, new CodeRetriever(""))
            );

            if (result.size() == 0) {
                logger.warn("No codes found for user with ID {} and with account ID {}.", id, accountID);
            } else {
                logger.trace("Codes retrieved.");
            }
        }

        return codes;
    }

    /**
     * Get a code for an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param codeID ID of the code.
     * @return The code.
     */
    public static Code getCode(Token token, int codeID) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to retrieve code with ID {} for user with ID {}.", codeID, id);

            Code c = sqLiteHelper.executeSelect(
                """
                    SELECT id, code FROM codes
                    WHERE id = ?
                    """,
                    new IntegerSetter(codeID),
                    new CodeRetriever("")
            );

            if (c == null) {
                logger.warn("Failed to retrieve code with ID {} for user with ID {}. Returning null.", codeID, id);
                return null;
            }

            logger.trace("Retrieved code.");
            return c;
        }

        return null;
    }

    /**
     * Add a code for an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the code.
     * @param code Code to add.
     * @return ID of the new code.
     */
    public static int addCode(Token token, int accountID, String code) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to add code {} to account with ID {} for user wih ID {}.", code, accountID, id);

            Integer i = sqLiteHelper.executeUpdateWithKey(
                """
                    INSERT INTO codes
                    (account_id, code)
                    VALUES
                    (?, ?)
                    """,
                    new IntegerSetter(accountID),
                    new StringSetter(code)
            );

            if (i == null) {
                logger.warn("Update failed. Returning -1.");
                return -1;
            }

            logger.trace("Update complete.");
            return i;
       }

        return -1;
    }

    /**
     * Update the code for an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param codeID ID of the code.
     * @param newCode Code to update to.
     */
    public static void updateCode(Token token, int codeID, String newCode) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to update code with ID {} for user with ID {} to {}.", codeID, id, newCode);

            sqLiteHelper.executeUpdate(
                """
                    UPDATE codes
                    SET code = ?
                    WHERE id = ?
                    """,
                    new StringSetter(newCode),
                    new IntegerSetter(codeID)
            );

            logger.trace("Update complete.");
        }
    }

    /**
     * Remove a code from an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param codeID ID of the code to remove.
     */
    public static void removeCode(Token token, int codeID) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to delete code with ID {} for user with ID {}.", codeID, id);

            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM codes
                    WHERE id = ?
                    """,
                    new IntegerSetter(codeID)
            );

            logger.trace("Update complete.");
        }
    }

    /**
     * Clear all codes for an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the account which contains the backup codes.
     */
    public static void clearAllCodes(Token token, int accountID) {
        if (safetyChecks(token)) {
            int id = ((CustomToken) token).ID;
            logger.trace("Attempting to delete all codes for account with ID {} for user with ID {}.", accountID, id);

            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM codes
                    WHERE account_id = ?
                    """,
                    new IntegerSetter(accountID)
            );

            logger.trace("Update complete.");
        }
    }

    /**
     * Get the name of the theme (in string format) based on the ID.
     *
     * @param id ID of the theme.
     * @return Name of the theme as a string.
     */
    private static String getThemeByID(int id) {
        if (sqLiteHelper.connectionFailure()) {
            return null;
        }

        if (id < 1 || id > 3) {
            logger.warn("Theme with ID {} does not exist. Must either be 1, 2, or 3. Returning null.", id);
            return null;
        }

        String result = sqLiteHelper.executeSelect(
            """
                SELECT name FROM themes
                WHERE id = ?
                """,
                new IntegerSetter(id),
                new StringRetriever("name")
        );

        if (result == null) {
            logger.warn("Failed to retrieve theme with ID {}. Returning null.", id);
            return null;
        }

        logger.trace("Theme retrieved.");
        return result;
    }

    /**
     * Get the preferred theme for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @return The theme for the user.
     */
    public static String getTheme(Token token) {
        if (safetyChecks(token)) {
            int userID = ((CustomToken) token).ID;
            logger.trace("Retrieving theme for user with ID {}.", userID);

            Integer id = sqLiteHelper.executeSelect(
                """
                    SELECT theme_id FROM users
                    WHERE id = ?
                    """,
                    new IntegerSetter(userID),
                    new IntegerRetriever("theme_id")
            );

            if (id == null) {
                logger.warn("Failed to retrieve ID for theme for user with ID {}. Returning null.", userID);
                return null;
            }

            return getThemeByID(id);
        }

        return null;
    }

    /**
     * Set the preferred theme for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param newTheme The new theme to set for the user.
     */
    public static void updateTheme(Token token, String newTheme) {
        if (safetyChecks(token)) {
            int userID = ((CustomToken) token).ID;
            logger.trace("Updating theme to {} for user with ID {}.", newTheme, userID);

            Integer id = sqLiteHelper.executeSelect(
                """
                    SELECT id FROM themes
                    WHERE name = ?
                    """,
                    new StringSetter(newTheme),
                    new IntegerRetriever("id")
            );

            if (id == null) {
                logger.warn("Failed to retrieve ID for theme {}. Aborting request.", newTheme);
                return;
            }

            sqLiteHelper.executeUpdate(
                """
                    UPDATE users
                    SET theme_id = ?
                    WHERE id = ?
                    """,
                    new IntegerSetter(id),
                    new IntegerSetter(userID)
            );

            logger.trace("Update complete.");
        }
    }

    /**
     * Set the login status for a user. To remember
     * whether if the user logged out before closing the
     * application.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param status The new status.
     */
    private static void updateLoginStatus(Token token, boolean status) {
        if (safetyChecks(token)) {
            sqLiteHelper.executeUpdate(
                """
                    UPDATE users
                    SET logged_in = ?
                    WHERE id = ?
                    """,
                    new IntegerSetter(status ? 1 : 0),
                    new IntegerSetter(((CustomToken) token).ID)
            );
        }
    }

}