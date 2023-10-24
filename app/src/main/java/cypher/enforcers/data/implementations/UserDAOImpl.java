package cypher.enforcers.data.implementations;

import cypher.enforcers.data.entities.User;
import cypher.enforcers.views.themes.Theme;
import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation for the User Data Access Object (DAO) to communicate
 * to the database and make changes to any information related to the users.
 */
public class UserDAOImpl implements UserDAO {

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
        System.out.println("Checking if username is taken.");
        return false;
    }

    /**
     * Register a new user.
     *
     * @param user The user to register.
     * @return True if the user was registered successfully, false otherwise.
     */
    @Override
    public boolean registerUser(User user) {
        if (userID == null || userID >= 1) {
            logger.error("Cannot create new user when there's a user currently logged in.");
            return false;
        }

        logger.trace("Now making update to the users table.");

        try {
            databaseService.inTransaction(entityManager ->
                    entityManager.persist(user)
            );
        } catch (Exception e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }

        userID = user.getID();
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

        System.out.println("Updating user");
        return false;
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
     * search is case-sensitive.
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
