package cypher.enforcers.data.implementations;

import cypher.enforcers.models.User;
import cypher.enforcers.utilities.sqliteutilities.retrievers.Retrievers;
import cypher.enforcers.views.themes.Theme;
import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation for the User Data Access Object (DAO) to communicate
 * to the database and make changes to any information related to the users.
 */
public class UserDAOImpl implements UserDAO {

    private static final String ADD_USER_QUERY = "INSERT INTO USERS (username, password) VALUES (?, ?) RETURNING id";

    private static final String UPDATE_THEME = "UPDATE users SET theme_value = ? WHERE id = ?";

    private static final String CHECK_USERNAME = "SELECT ( COUNT(*) > 0 ) AS 'contains' FROM users WHERE username = ?";

    // Logger for the user data access object.
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    // Service to communicate to the database.
    @SimpleService
    private DatabaseService databaseService;


    // Cached userID to avoid having to check if someone is logged in or
    // not.
    // Must be >= 1 if user is logged in, -1 otherwise if no user is
    // logged in, and null if application just started up.
    private Long userID = null;

    /**
     * Check if a given username is taken. The check is case-sensitive.
     *
     * @param username The username to check.
     * @return True if the username is taken, false otherwise.
     */
    @Override
    public boolean checkUsername(String username) {
        try {
            Boolean bool = databaseService.executeSelect(
                    CHECK_USERNAME,
                    Retrievers.ofBoolean("contains"),
                    username.toLowerCase()
            );

            if (bool == null || !bool) {
                return false;
            }
        } catch (SQLException e) {
            logger.debug("Failed select query. Cause: ", e);
            return false;
        }

        return true;
    }

    /**
     * Register a new user.
     *
     * @param username The new username for this user.
     * @param password The new password for this user.
     * @return True if the user was registered successfully, false otherwise.
     */
    @Override
    public boolean registerUser(String username, String password) {
        if (userID == null || userID >= 1) {
            logger.error("Cannot create new user when there's a user currently logged in.");
            return false;
        }

        logger.trace("Now making update to the users table.");

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            databaseService.executeUpdate(ADD_USER_QUERY, user);

            userID = user.getID();
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }

        return true;
    }

    /**
     * Get the current logged-in user.
     *
     * @return An Optional containing the user if found, null otherwise.
     */
    @Override
    public Optional<User> getUser() {
        if (userID == null) {
            // Check if there is any user even logged in.
            // If there is, set the ID and return the object.
            // Otherwise, set the ID to -1 and return an empty
            // optional.
        } else if (userID == -1) {
            return Optional.empty();
        } else {
            // Try to find the user and return it.
        }
        userID = -1L;
        System.out.println("Getting user with id = " + userID);
        return Optional.empty();
    }

    /**
     * Get the theme for the current logged-in user.
     *
     * @return An Optional containing the theme if found, null otherwise.
     */
    @Override
    public Optional<Theme> getTheme() {
        if (userID == null || userID <= 0) {
            return Optional.empty();
        }

        System.out.println("Getting theme");
        return Optional.empty();
    }

    /**
     * Update the theme of the current logged-in user.
     *
     * @param newTheme The new theme.
     * @return True if the theme was updated successfully, false otherwise.
     */
    @Override
    public boolean updateTheme(Theme newTheme) {
        if (userID == null || userID <= 0) {
            return false;
        }

        try {
            databaseService.executeUpdate(UPDATE_THEME, newTheme.ordinal(), userID);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }
        return true;
    }

    /*
     The methods below are internal methods hidden from the interface.
     Use them carefully.
     */

    /**
     * Update the login status for a user to be logged in.
     *
     * @return True if the status was updated, false otherwise.
     */
    public boolean loginUser(long userID) {
        if (userID <= 0) {
            return false;
        }

        System.out.println("Updating login status");
        return false;
    }

    /**
     * Update the login status for the current logged-in user to
     * be logged-out.
     *
     * @return True if the status was updated or if no user is logged in,
     * false otherwise.
     */
    public boolean logoutUser() {
        if (userID == null || userID <= 0) {
            return true;
        }

        System.out.println("Updating login status");
        return false;
    }

    /**
     * Get the password and ID of a user given their username. The
     * search is case-insensitive.
     *
     * @param username Username to search for.
     * @return A Map containing the following key-value pairs:
     *          "password" : password of the user as a string
     *          "id" : id of the user as a string
     * If no user is found, an empty map is returned instead. Lastly, if
     * any issues come along the way, null will be returned.
     */
    public Map<String, String> getUserData(String username) {
        System.out.println("Getting user data");
        return Collections.emptyMap();
    }

    /**
     * Get the ID of the current logged-in user.
     *
     * @return ID of the user logged-in and -1 if no use is logged in.
     */
    public long getUserID() {
        return userID == null ? -1L : userID;
    }
}
