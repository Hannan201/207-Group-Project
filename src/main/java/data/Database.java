package data;

import javafx.application.Application;
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
import java.nio.file.Files;
import java.nio.file.Path;
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
            boolean tablesSet = Files.exists(Path.of(filename));
            SQLiteConfig configurations = new SQLiteConfig();
            configurations.enforceForeignKeys(true);

            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + filename,
                    configurations.toProperties()
            );

            if (!tablesSet) {
                initializeTables(connection);
            }
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the tables for the database. This is assuming
     * a database file is being created for the first time and
     * one does not already exist.
     *
     * @param connection Connection to the database.
     */
    private static void initializeTables(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            // Table for themes.
            statement.execute(
                    """
                        CREATE TABLE themes(
                            id INTEGER PRIMARY KEY,
                            name TEXT NOT NULL UNIQUE
                        );
                       """
            );

            // Add in the themes.
            statement.executeUpdate(
                    """
                       INSERT INTO themes
                       (name)
                       VALUES 
                       ('light mode'),
                       ('dark mode'),
                       ('high contrast mode');
                       """
            );

            // Table for users.
            statement.execute(
                    """
                        CREATE TABLE users(
                            id INTEGER PRIMARY KEY,
                            username TEXT NOT NULL UNIQUE CHECK ( length(username) > 0 ),
                            password TEXT NOT NULL CHECK ( length(password) > 0 ),
                            theme_id INT NOT NULL DEFAULT 1 CHECK ( theme_id = 1 OR theme_id = 2 OR theme_id = 3 ),
                            logged_in INT NOT NULL DEFAULT 1 CHECK ( logged_in = 0 OR logged_in = 1 ),
                            FOREIGN KEY(theme_id) REFERENCES themes(id)
                        );
                       """
            );

            // Table for accounts.
            statement.execute(
                    """
                        CREATE TABLE accounts(
                            id INTEGER PRIMARY KEY,
                            user_id INT NOT NULL,
                            name TEXT NOT NULL CHECK ( length(name) > 0 ),
                            type TEXT NOT NULL CHECK ( length(type) > 0 ),
                            FOREIGN KEY(user_id) REFERENCES users(id)
                        );
                       """
            );

            // Table for codes.
            statement.execute(
                    """
                        CREATE TABLE codes(
                            id INTEGER PRIMARY KEY,
                            account_id INT NOT NULL,
                            code TEXT NOT NULL CHECK ( length(code) > 0 ),
                            FOREIGN KEY(account_id) REFERENCES accounts(id)
                        );
                       """
            );
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

    }

    /*
     These methods are for signing-up, signing-in, and signing-out.
     */

    private static boolean authenticateToken(Token token) {
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
     * @return True if username is taken, false otherwise.s
     */
    public static boolean checkUsername(String username) {
        if (username.isEmpty() || connection == null || username.isBlank()) {
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

    private static class TokenManager {
        private static KeyStore storage;
        private static char[] password;

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

                Key key = storage.getKey("token", password);
                if (key == null) {
                    updateToken("");
                }
            } catch (UnrecoverableKeyException | KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
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

    private static class CustomToken implements Token {
        private final String key;
        private final int ID;

        public CustomToken(String key, int ID) {
            this.key = key;
            this.ID = ID;
        }

        public String getKey() {
            return this.key;
        }

        public int getID() {
            return this.ID;
        }
    }

    /**
     * Register a new user for this application.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     */
    public static Token registerUser(String username, String password) {
        if (connection != null) {
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
     * @return True if the user was logged-in, false otherwise.
     */
    public static boolean authenticateUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        if (!checkUsername(username)) {
            return false;
        }

//        List<String> configurations = userConfigurations.get(username.toLowerCase());
//        String salt = configurations.get(1);
//        loggedIn = passwordHasher.verifyPassword(configurations.get(0), password, salt);
//        if (loggedIn) {
//            user = users.get(username.toLowerCase());
//            setLoginStatus("true");
//        } else {
//            user = null;
//        }
//
//        return loggedIn;

        return true;
    }

    /**
     * Log out a user from this application.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static void logUserOut(Token token) {
        updateLoginStatus(token, false);
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
        if (authenticateToken(token)) {
            if (connection != null) {
                PreparedStatement getUserStatement = null;
                PreparedStatement getThemeNameStatement = null;
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
                    int themeID = resultKeys.getInt("theme_id");

                    getThemeNameStatement = connection.prepareStatement(
                            """
                                SELECT name FROM themes
                                WHERE id = ?
                               """
                    );
                    getThemeNameStatement.setInt(1, themeID);
                    ResultSet themeResult = getThemeNameStatement.executeQuery();
                    String theme = themeResult.getString("name");
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
        return 0;
    }

    /**
     * Remove an account for a user give the ID.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the account.
     */
    public static void removeAccount(Token token, int accountID) {

    }

    /**
     * Clear all accounts for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     */
    public static void clearAllAccounts(Token token) {

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
        return 0;
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

    }

    /**
     * Remove a code from an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param codeID ID of the code to remove.
     */
    public static void removeCode(Token token, int codeID) {

    }

    /**
     * Clear all codes for an account.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @param accountID ID of the account which contains the backup codes.
     */
    public static void clearAllCodes(Token token, int accountID) {

    }

    /**
     * Get the preferred theme for a user.
     *
     * @param token Token given to the user once registered/logged in,
     *              for authentication purposes.
     * @return The theme for the user.
     */
    public static String getTheme(Token token) {
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

    }
}