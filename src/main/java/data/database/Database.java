package data.database;

import data.security.PasswordHasher;
import data.Storage;
import data.security.Token;
import data.security.TokenGenerator;
import models.Account;
import models.Code;
import models.User;
import org.sqlite.SQLiteConfig;
import utilities.ResourceUtilities;

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

    // Holds the connection to the SQLite database.
    private static Connection connection = null;

    // To generate a token for the user.
    private static TokenGenerator generator;

    /*
     These methods are for connecting and
     configuring the SQLite database.
     */

    /**
     * The SQLite file this database needs to connect to.
     *
     * @param source Name of the database file.
     */
    public static void setConnectionSource(String source) {
        if (connection == null && !source.isEmpty() && !source.isBlank() && source.endsWith(".db")) {
            connect(source);
            TokenManager.initializeStorage();
            passwordHasher = new PasswordHasher();
            generator = new TokenGenerator(21);
        }
    }

    /**
     * Open a connection to the database.
     *
     * @param filename Name of the database file.
     */
    private static void connect(String filename) {
        try {
            SQLiteConfig configurations = new SQLiteConfig();
            configurations.enforceForeignKeys(true);

            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + filename,
                    configurations.toProperties()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from the database.
     */
    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     These methods are for signing-up, signing-in, and signing-out.
     */

    /**
     * Check if a token doesn't match what's currently
     * stored. This is to prevent access from unknown users.
     *
     * @param token The token.
     * @return True if the token does not match, false otherwise.
     */
    private static boolean invalidToken(Token token) {
        String[] result = Objects.requireNonNull(TokenManager.parseToken());
        if (result.length != 2) {
            return false;
        }

        CustomToken casted = (CustomToken) token;
        return !result[0].equals(casted.key)
                || Integer.parseInt(result[1]) != casted.ID;
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

        if (connection == null) {
            return true;
        }

        PreparedStatement searchStatement = null;
        try {
            searchStatement = connection.prepareStatement(
                    """
                        SELECT (COUNT(*) > 0) AS 'contains' FROM users
                        WHERE username = ?
                       """
            );
            searchStatement.setString(1, username.toLowerCase());
            ResultSet results = searchStatement.executeQuery();

            return results.getBoolean("contains");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (searchStatement != null) {
                    searchStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
                URL url = ResourceUtilities.loadFileByURL("application.properties");
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
                URL url = ResourceUtilities.loadFileByURL("token.pfx");
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
                URL url = ResourceUtilities.loadFileByURL("token.pfx");
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
        if (connection == null) {
            return null;
        }

        PreparedStatement addUserStatement = null;
        try {
            String salt = passwordHasher.generateSalt();
            String hashed = salt + passwordHasher.hashPassword(password + salt, salt.getBytes());
            addUserStatement = connection.prepareStatement(
                    """
                       INSERT INTO users
                       (username, password)
                       VALUES
                       (?, ?)
                       """,
                    Statement.RETURN_GENERATED_KEYS
            );

            addUserStatement.setString(1, username.toLowerCase());
            addUserStatement.setString(2, hashed);
            addUserStatement.executeUpdate();

            ResultSet resultKeys = addUserStatement.getGeneratedKeys();
            int id = 0;
            while (resultKeys.next()) {
                id = resultKeys.getInt(1);
            }

            String token = generator.nextString();
            TokenManager.updateToken(token + "," + id);
            return new CustomToken(token, id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (addUserStatement != null) {
                    addUserStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

        if (connection == null) {
            return null;
        }

        PreparedStatement getLoginStatement = null;
        try {
            getLoginStatement = connection.prepareStatement(
                    """
                        SELECT id, username, password FROM users
                        WHERE username = ?
                       """
            );

            getLoginStatement.setString(1, username.toLowerCase());

            ResultSet loginsResult = getLoginStatement.executeQuery();
            String hashedPassword = loginsResult.getString("password");

            String salt = hashedPassword.substring(0, 24);
            String expectedPassword = hashedPassword.substring(24);
            if (passwordHasher.verifyPassword(expectedPassword, password + salt, salt)) {
                String token = generator.nextString();
                int id = loginsResult.getInt("id");

                TokenManager.updateToken(token + "," + id);
                CustomToken customToken = new CustomToken(token, id);

                updateLoginStatus(customToken, true);

                return customToken;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getLoginStatement != null) {
                    getLoginStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }

        return null;
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
        if (invalidToken(token)) {
            return null;
        }

        if (connection == null) {
            return null;
        }

        PreparedStatement getUserStatement = null;
        try {
            getUserStatement = connection.prepareStatement(
                    """
                       SELECT * FROM users
                       WHERE id = ?
                       """
            );

            getUserStatement.setInt(1, ((CustomToken) token).ID);
            ResultSet resultKeys = getUserStatement.executeQuery();

            int id = resultKeys.getInt("id");
            String name = resultKeys.getString("username");
            String theme = getThemeByID(resultKeys.getInt("theme_id"));
            return new User(id, name, theme);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getUserStatement != null) {
                    getUserStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

        if (invalidToken(token)) {
            return accounts;
        }

        if (connection == null) {
            return accounts;
        }

        PreparedStatement getAccountsStatement = null;
        try {
            getAccountsStatement = connection.prepareStatement(
                    """
                       SELECT id, name, type FROM accounts
                       WHERE user_id = ?
                       """
            );
            int id = ((CustomToken) token).ID;
            getAccountsStatement.setInt(1, id);

            ResultSet results = getAccountsStatement.executeQuery();

            while (results.next()) {
                int accountID = results.getInt("id");
                String name = results.getString("name");
                String type = results.getString("type");
                accounts.add(
                        new Account(accountID, name, type)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getAccountsStatement != null) {
                    getAccountsStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
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
        if (invalidToken(token)) {
            return -1;
        }

        if (connection == null) {
            return -1;
        }

        PreparedStatement addAccountStatement = null;
        try {
            addAccountStatement = connection.prepareStatement(
                    """
                       INSERT INTO accounts
                       (user_id, name, type)
                       VALUES
                       (?, ?, ?)
                       """,
                    Statement.RETURN_GENERATED_KEYS
            );

            int id = ((CustomToken) token).ID;
            addAccountStatement.setInt(1, id);
            addAccountStatement.setString(2, name);
            addAccountStatement.setString(3, type);
            addAccountStatement.executeUpdate();

            ResultSet resultKeys = addAccountStatement.getGeneratedKeys();
            while (resultKeys.next()) {
                id = resultKeys.getInt(1);
            }

            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (addAccountStatement != null) {
                    addAccountStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement removeAccountStatement = null;
        try {
            removeAccountStatement = connection.prepareStatement(
                    """
                       DELETE FROM accounts
                       WHERE id = ?
                       """
            );
            removeAccountStatement.setInt(1, accountID);
            removeAccountStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (removeAccountStatement != null) {
                    removeAccountStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Clear all accounts for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static void clearAllAccounts(Token token) {
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement clearUserStatement = null;
        try {
            clearUserStatement = connection.prepareStatement(
                    """
                        DELETE FROM accounts
                        WHERE user_id = ?
                       """
            );
            int id = ((CustomToken) token).ID;
            clearUserStatement.setInt(1, id);
            clearUserStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clearUserStatement != null) {
                    clearUserStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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

        if (invalidToken(token)) {
            return codes;
        }

        if (connection == null) {
            return codes;
        }

        PreparedStatement getCodesStatement = null;
        try {
            getCodesStatement = connection.prepareStatement(
                    """
                        SELECT id, code FROM codes
                        WHERE account_id = ?
                       """
            );
            getCodesStatement.setInt(1, accountID);

            ResultSet results = getCodesStatement.executeQuery();

            while (results.next()) {
                int id = results.getInt("id");
                String code = results.getString("code");
                codes.add(
                        new Code(id, code)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getCodesStatement != null) {
                    getCodesStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
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
        if (invalidToken(token)) {
            return null;
        }

        if (connection == null) {
            return null;
        }

        PreparedStatement getCodeStatement = null;
        try {
            getCodeStatement = connection.prepareStatement(
                    """
                        SELECT id, code FROM codes
                        WHERE id = ?
                       """
            );
            getCodeStatement.setInt(1, codeID);
            ResultSet codes = getCodeStatement.executeQuery();
            if (codes.next()) {
                int id = codes.getInt("id");
                String code = codes.getString("code");
                return new Code(id, code);
            }

            return null;
        } catch (SQLException e) {
            try {
                if (getCodeStatement != null) {
                    getCodeStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return -1;
        }

        if (connection == null) {
            return -1;
        }

        PreparedStatement addCodeStatement = null;
        try {
            addCodeStatement = connection.prepareStatement(
                    """
                       INSERT INTO codes
                       (account_id, code)
                       VALUES
                       (?, ?)
                       """,
                    Statement.RETURN_GENERATED_KEYS
            );

            addCodeStatement.setInt(1, accountID);
            addCodeStatement.setString(2, code);
            addCodeStatement.executeUpdate();

            ResultSet resultKeys = addCodeStatement.getGeneratedKeys();
            int id = 0;
            while (resultKeys.next()) {
                id = resultKeys.getInt(1);
            }

            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (addCodeStatement != null) {
                    addCodeStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement updateCodeStatement = null;
        try {
            updateCodeStatement = connection.prepareStatement(
                    """
                        UPDATE codes
                        SET code = ?
                        WHERE id = ?
                       """
            );
            updateCodeStatement.setString(1, newCode);
            updateCodeStatement.setInt(2, codeID);
            updateCodeStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (updateCodeStatement != null) {
                    updateCodeStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement removeCodeStatement = null;
        try {
            removeCodeStatement = connection.prepareStatement(
                    """
                        DELETE FROM codes
                        WHERE id = ?
                       """
            );
            removeCodeStatement.setInt(1, codeID);
            removeCodeStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (removeCodeStatement != null) {
                    removeCodeStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement clearCodesStatement = null;
        try {
            clearCodesStatement = connection.prepareStatement(
                    """
                        DELETE FROM codes
                        WHERE account_id = ?
                       """
            );
            clearCodesStatement.setInt(1, accountID);
            clearCodesStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clearCodesStatement != null) {
                    clearCodesStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Get the name of the theme (in string format) based on the ID.
     *
     * @param id ID of the theme.
     * @return Name of the theme as a string.
     */
    private static String getThemeByID(int id) {
        if (connection == null) {
            return null;
        }

        if (id < 1 || id > 3) {
            return null;
        }

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    """
                        SELECT name FROM themes
                        WHERE id = ?
                       """
            );
            statement.setInt(1, id);
            ResultSet themeResult = statement.executeQuery();
            return themeResult.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get the preferred theme for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @return The theme for the user.
     */
    public static String getTheme(Token token) {
        if (invalidToken(token)) {
            return null;
        }

        if (connection == null) {
            return null;
        }

        PreparedStatement getThemeStatement = null;
        try {
            getThemeStatement = connection.prepareStatement(
                    """
                        SELECT theme_id FROM users
                        WHERE id = ?
                       """
            );
            int id = ((CustomToken) token).ID;
            getThemeStatement.setInt(1, id);
            ResultSet results = getThemeStatement.executeQuery();

            return getThemeByID(results.getInt("theme_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getThemeStatement != null) {
                    getThemeStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement updateThemeStatement = null;
        PreparedStatement themeIDStatement = null;
        try {
            updateThemeStatement = connection.prepareStatement(
                    """
                        UPDATE users
                        SET theme_id = ?
                        WHERE id = ?
                       """
            );

            themeIDStatement = connection.prepareStatement(
                    """
                        SELECT id FROM themes
                        WHERE name = ?
                       """
            );
            themeIDStatement.setString(1, newTheme);

            ResultSet themeResults = themeIDStatement.executeQuery();

            int themeID = themeResults.getInt("id");
            int userID = ((CustomToken) token).ID;

            updateThemeStatement.setInt(1, themeID);
            updateThemeStatement.setInt(2, userID);
            updateThemeStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (updateThemeStatement != null) {
                    updateThemeStatement.close();
                }

                if (themeIDStatement != null) {
                    themeIDStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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
        if (invalidToken(token)) {
            return;
        }

        if (connection == null) {
            return;
        }

        PreparedStatement logoutStatement = null;
        try {
            logoutStatement = connection.prepareStatement(
                    """
                        UPDATE users
                        SET logged_in = ?
                        WHERE id = ?
                       """
            );

            int result = status ? 1 : 0;
            logoutStatement.setInt(1, result);
            int id = ((CustomToken) token).ID;
            logoutStatement.setInt(2, id);
            logoutStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (logoutStatement != null) {
                    logoutStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }
}