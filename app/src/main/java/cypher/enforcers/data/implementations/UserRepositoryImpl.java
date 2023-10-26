package cypher.enforcers.data.implementations;

import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.UserRepository;
import cypher.enforcers.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.views.themes.Theme;
import cypher.enforcers.annotations.SimpleService;

import java.util.Map;
import java.util.Optional;

/**
 * Implementation for the User Data Access Object (DAO) to communicate to
 * the database and make changes to any information related to the users.
 */
public class UserRepositoryImpl implements UserRepository {

    // Logger for the user repository.
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    // To make updates to the database.
    @SimpleService
    private UserDAOImpl userDAO;

    // Used to hash password.
    @SimpleService
    private PasswordHasher passwordHasher;

    /**
     * Create a new user. Usernames are case-insensitive.
     *
     * @param username The new username for this user.
     * @param password The new password for this user.
     * @return True if the user was successfully created, false
     * otherwise.
     */
    @Override
    public boolean create(String username, String password) {
        logger.trace("Attempting to create user with username {}.", username);

        if (username.isBlank() || username.isEmpty()
            || password.isBlank() || password.isEmpty()) {
            logger.warn("Username or password cannot be empty.");
            return false;
        }

        password = hashPassword(password);

        boolean result = userDAO.registerUser(username.toLowerCase(), password);

        if (result) {
            logger.trace("User created.");
        } else {
            logger.warn("Failed to create user with username {}.", username);
        }

        return result;
    }

    /**
     * Read the current user logged in.
     *
     * @return An Optional containing the user. Null otherwise.
     */
    @Override
    public Optional<User> read() {
        logger.trace("Attempting to get the current logged in user.");

        Optional<User> result = userDAO.getUser();

       if (result.isPresent()) {
           logger.trace("User found.");
       } else {
            logger.warn("Unable to get the current logged in user.");
       }

        return result;
    }

    /**
     * Read the theme for the user logged in.
     *
     * @return An Optional containing the theme. Null otherwise.
     */
    @Override
    public Optional<Theme> readTheme() {
        logger.trace("Attempting to get theme for the current logged in user.");

        Optional<Theme> result = userDAO.getTheme();

        if (result.isPresent()) {
            logger.trace("Theme found.");
        } else {
            logger.warn("Failed to retrieve theme for the current logged in user.");
        }

        return result;
    }

    /**
     * Update the theme for the user logged in.
     *
     * @return True if the theme was updated, false otherwise.
     */
    @Override
    public boolean update(Theme newTheme) {
        logger.trace("Attempting to update theme to {} for the current logged in user.", newTheme);

        boolean result = userDAO.updateTheme(newTheme);

        if (result) {
            logger.trace("Theme updated successfully.");
        } else {
            logger.warn("Unable to update theme {} to for the current logged in user.", newTheme);
        }

        return result;
    }

    /**
     * Check if a given username is taken. The search is
     * case-insensitive.
     *
     * @param username The username to search for.
     * @return True if the user is taken, false otherwise.
     */
    @Override
    public boolean checkUsername(String username) {
        logger.trace("Attempting to check if username {} is taken.", username);

        if (username.isEmpty() || username.isBlank()) {
            logger.warn("Username cannot be empty.");
            return true;
        }

        boolean result = userDAO.checkUsername(username.toLowerCase());

        if (result) {
            logger.trace("Username taken.");
        } else {
            logger.trace("Username is not taken.");
        }

        return result;
    }

    /*
     The methods below are internal methods hidden from the interface.
     Use them carefully.
     */

    /**
     * Hash password for the user.
     *
     * @param password The password to hash.
     */
    private String hashPassword(String password) {
        String salt = passwordHasher.generateSalt();
        return salt + passwordHasher.hashPassword(password + salt, salt.getBytes());
    }

    /**
     * Get the password and ID of a user given their username. The
     * search is case-insensitive. This method is usually called when
     * trying to authenticate a user.
     *
     * @param username Username to search for.
     * @return A Map containing the following key-value pairs:
     *          "password" : password of the user as a string
     *          "id" : id of the user as a string
     * If no user is found, an empty map is returned instead. Lastly, if
     * any issues come along the way, null will be returned.
     */
    public Map<String, String> read(String username) {
        logger.trace("Fetching user data for user with username {}.", username);

        Map<String, String> result = userDAO.getUserData(username.toLowerCase());

        if (result == null) {
            logger.trace("Unable to find user data for user with username {}.", username);
        }

        logger.trace("Data found.");
        return result;
    }

    /**
     * Update the login status for a user to be logged in.
     *
     * @return True if the status was updated, false otherwise.
     */
    public boolean loginUser(long userID) {
        if (userID <= 0) {
            return false;
        }

        return userDAO.loginUser(userID);
    }

    /**
     * Logout the user from this application that's currently logged in.
     *
     * @return True if the current logged-in user was logged out or if
     * no user is logged-in, false otherwise.
     */
    public boolean logUserOut() {
        return userDAO.logoutUser();
    }

    /**
     * Get ID of the user currently logged in.
     *
     * @return ID of the current user.
     */
    public long getLoggedInUser() {
        return userDAO.getUserID();
    }

    /**
     * Get the object used for hashing passwords.
     *
     * @return The object that hashes passwords.
     */
    public PasswordHasher getPasswordHasher() {
        return this.passwordHasher;
    }
}
