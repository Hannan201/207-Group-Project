package data.database;

import data.*;
import data.security.PasswordHasher;
import data.security.Token;
import data.security.TokenGenerator;
import models.Account;
import code.readers.Code;
import models.User;
import utilities.Utilities;
import utilities.sqliteutilities.SQLiteHelper;
import utilities.sqliteutilities.argumentsetters.IntegerSetter;
import utilities.sqliteutilities.argumentsetters.StringSetter;
import utilities.sqliteutilities.retrievers.CodeRetriever;
import utilities.sqliteutilities.retrievers.IntegerRetriever;
import utilities.sqliteutilities.retrievers.ListRetriever;
import utilities.sqliteutilities.retrievers.StringRetriever;

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

    // To hash passwords which the user enters.
    private static PasswordHasher passwordHasher;

    // To generate a token for the user.
    private static TokenGenerator generator;

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
        if (!source.isEmpty() && !source.isBlank() && source.endsWith(".db")) {
            TokenManager.initializeStorage();
            passwordHasher = new PasswordHasher();
            generator = new TokenGenerator(21);
            sqLiteHelper = new SQLiteHelper(source);
        }
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
            return false;
        }

        CustomToken casted = (CustomToken) token;
        return result[0].equals(casted.key)
                && Integer.parseInt(result[1]) == casted.ID;
    }

    /**
     * Check if a username is already taken.
     *
     * @param username The username to check for.
     * @return True if username is taken, false otherwise.
     */
    public static boolean checkUsername(String username) {
        if (username.isEmpty() || username.isBlank()) {
            return true;
        }

        if (!sqLiteHelper.connectionFailure()) {
            return sqLiteHelper.executeSelect(
                """
                    SELECT (COUNT(*) > 0) AS 'contains' FROM users
                    WHERE username = ?
                    """,
                    new StringSetter(username.toLowerCase()),
                    resultSet -> {
                        try {
                            return resultSet.getBoolean("contains");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
            );
        }

        return true;
    }

    /*
     This class is responsible for loading and saving the current token
     to protect the database from unauthorised access.
     */
    private static class TokenManager {
        // Connection to the keystore object.
        private static KeyStore storage;

        // Password for the keystore.
        private static char[] password;

        /**
         * Load the token into storage, if there's any present.
         */
        private static void initializeStorage() {
            String[] result = Objects.requireNonNull(parseToken());

            if (result.length != 2) {
                return;
            }

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
            FileInputStream applicationFile = null;
            try {
                URL url = Utilities.loadFileByURL("application.properties");
                applicationFile = new FileInputStream(url.getPath());

                Properties applicationProperties = new Properties();
                applicationProperties.load(applicationFile);
                password = Objects.requireNonNull(
                        applicationProperties.getProperty("password")
                ).toCharArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (applicationFile != null) {
                        applicationFile.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }

        /**
         * Initialize the key store file.
         */
        private static void initializeKeyStore() {
            FileInputStream stream = null;
            try {
                URL url = Utilities.loadFileByURL("token.pfx");
                stream = new FileInputStream(url.getPath());
                storage = KeyStore.getInstance(KeyStore.getDefaultType());
                storage.load(stream, password);
            } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
                SecretKey secureKey = factory.generateSecret(
                        new PBEKeySpec(newToken.toCharArray())
                );

                KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secureKey);
                KeyStore.ProtectionParameter keyStorePassword = new KeyStore.PasswordProtection(password);
                storage.setEntry("token", secret, keyStorePassword);
                URL url = Utilities.loadFileByURL("token.pfx");
                storeOutput = new FileOutputStream(url.getPath());
                storage.store(storeOutput, password);
            } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | KeyStoreException |
                     CertificateException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (storeOutput != null) {
                        storeOutput.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
                KeyStore.ProtectionParameter keyStorePassword = new KeyStore.PasswordProtection(password);
                KeyStore.SecretKeyEntry key = (KeyStore.SecretKeyEntry) storage.getEntry("token", keyStorePassword);
                PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(
                        key.getSecretKey(),
                        PBEKeySpec.class
                );

                return new String(keySpec.getPassword()).split(",");
            } catch (InvalidKeySpecException | UnrecoverableEntryException | KeyStoreException | NoSuchAlgorithmException e) {
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
                return new CustomToken(token, i);
            }

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
            return null;
        }

        if (!checkUsername(username)) {
            return null;
        }

        if (sqLiteHelper.connectionFailure()) {
            return null;
        }

        return sqLiteHelper.executeSelect(
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
                            String token = generator.nextString();
                            int id = resultSet.getInt("id");

                            TokenManager.updateToken(token + "," + id);
                            CustomToken customToken = new CustomToken(token, id);

                            updateLoginStatus(customToken, true);

                            return customToken;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
        );
    }

    /**
     * Log out a user from this application.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static void logUserOut(Token token) {
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
            return sqLiteHelper.executeSelect(
                """
                    SELECT * FROM users
                    WHERE id = ?
                    """,
                    new IntegerSetter(((CustomToken) token).ID),
                    resultSet -> {
                        try {
                            int id = resultSet.getInt("id");
                            String name = resultSet.getString("username");
                            String theme = getThemeByID(resultSet.getInt("theme_id"));
                            return new User(id, name, theme);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
            );
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
            sqLiteHelper.executeSelect(
                """
                    SELECT id, name, type FROM accounts
                    WHERE user_id = ?
                    """,
                    new IntegerSetter(((CustomToken) token).ID),
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
        List<Account> accounts = Database.getAccounts(token);

        for (Account temp : accounts) {
            if (temp.getID() == accountID) {
                return temp;
            }
        }

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
        List<Account> accounts = Database.getAccounts(token);

        for (Account temp : accounts) {
            if (temp.getName().equals(name)) {
                return temp;
            }
        }

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

            return i == null ? -1 : i;
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
            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM accounts
                    WHERE id = ?
                    """,
                    new IntegerSetter(accountID)
            );
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
            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM accounts
                    WHERE user_id = ?
                    """,
                    new IntegerSetter(((CustomToken) token).ID)
            );
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
            return sqLiteHelper.executeSelect(
                """
                    SELECT id, code FROM codes
                    WHERE account_id = ?
                    """,
                    new IntegerSetter(accountID),
                    new ListRetriever<>(codes, new CodeRetriever(""))
            );
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
            return sqLiteHelper.executeSelect(
                """
                    SELECT id, code FROM codes
                    WHERE id = ?
                    """,
                    new IntegerSetter(codeID),
                    new CodeRetriever("")
            );
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

            return i == null ? -1 : i;
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
            sqLiteHelper.executeUpdate(
                """
                    UPDATE codes
                    SET code = ?
                    WHERE id = ?
                    """,
                    new StringSetter(newCode),
                    new IntegerSetter(codeID)
            );
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
            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM codes
                    WHERE id = ?
                    """,
                    new IntegerSetter(codeID)
            );
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
            sqLiteHelper.executeUpdate(
                """
                    DELETE FROM codes
                    WHERE account_id = ?
                    """,
                    new IntegerSetter(accountID)
            );
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
            return null;
        }

        return sqLiteHelper.executeSelect(
            """
                SELECT name FROM themes
                WHERE id = ?
                """,
                new IntegerSetter(id),
                new StringRetriever("name")
        );
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
            int id = sqLiteHelper.executeSelect(
                """
                    SELECT theme_id FROM users
                    WHERE id = ?
                    """,
                    new IntegerSetter(((CustomToken) token).ID),
                    new IntegerRetriever("theme_id")
            );

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
            int id = sqLiteHelper.executeSelect(
                """
                    SELECT id FROM themes
                    WHERE name = ?
                    """,
                    new StringSetter(newTheme),
                    new IntegerRetriever("id")
            );

            sqLiteHelper.executeUpdate(
                """
                    UPDATE users
                    SET theme_id = ?
                    WHERE id = ?
                    """,
                    new IntegerSetter(id),
                    new IntegerSetter(((CustomToken) token).ID)
            );
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