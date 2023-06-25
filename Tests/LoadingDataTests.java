import org.junit.jupiter.api.Test;
import models.Account;
import data.Database;
import models.User;
import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadingDataTests {
    String pathToUserFile = "Tests/users.ser";

    String pathToConfigFile = "Tests/configurations.ser";

    private void initializeTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            SQLiteConfig configurations = new SQLiteConfig();
            configurations.enforceForeignKeys(true);

            connection = DriverManager.getConnection(
                    "jdbc:sqlite:./Tests/test.db",
                    configurations.toProperties()
            );

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

            statement.executeUpdate(
                    """
                       INSERT INTO users
                       (username, password, theme_id, logged_in)
                       VALUES 
                       ('hannan', '1234', 1, 0);
                       """
            );

            statement.executeUpdate(
                    """
                       INSERT INTO accounts
                       (user_id, name, type)
                       VALUES 
                       (1, 'joe', '1234');
                       """
            );

            statement.executeUpdate(
                    """
                       INSERT INTO codes
                       (account_id, code)
                       VALUES 
                       (1, '1234'),
                       (1, '1234'),
                       (1, '1234');
                       """
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Test
    void userLoginLoaded() {
//        assertTrue(Database.authenticateUser("Hannan", "12345"));
//        assertTrue(Database.getLoginStatus());
//        Database.logUserOut();
//        initializeTable();
//
//        Database.setConnectionSource("./Tests/test.db");
//        assertTrue(Database.checkUsername("haNNan"));
//        //assertTrue(Database.authenticateUser("hannan", "12345"));
//
//        Database.disconnect();
//        try {
//            Files.deleteIfExists(Path.of("./Tests/test.db"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void userAccountLoaded() {
//        Database.setConfigurationsSource(pathToConfigFile);
//        Database.setUsersSource(pathToUserFile);
//        Database.authenticateUser("Hannan", "12345");
//        User user = Database.getUser();
//        assertNotNull(user);
//        assertEquals(user.getUsername(), "Hannan");
//        assertEquals(user.getAccounts().size(), 1);
//        Account accounts = user.getAccounts().get(0);
//        assertEquals(accounts.getName(), "Joe");
//        assertEquals(accounts.getSocialMediaType(), "1234");
//        assertEquals(accounts.getUserCodes().size(), 3);
//        List<String> codes = accounts.getUserCodes();
//        for (String s : codes) {
//            assertEquals(s, "1234");
//        }
    }
}