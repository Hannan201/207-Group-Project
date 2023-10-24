package cypher.enforcers.data.implementations;

import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.views.themes.Theme;
import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.data.entities.User;

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
     * @param user The user to create.
     * @return True if the user was successfully created, false
     * otherwise.
     */
    @Override
    public boolean create(User user) {
        logger.trace("Attempting to create user with username {}.", user.getUsername());

        if (user.getUsername().isBlank() || user.getUsername().isEmpty()
            || user.getPassword().isBlank() || user.getPassword().isEmpty()) {
            logger.warn("Username or password cannot be empty.");
            return false;
        }

        hashPassword(user);

        boolean result = userDAO.registerUser(user);

        if (result) {
            logger.trace("User created.");
        } else {
            logger.warn("Failed to create user with username {}.", user.getUsername());
        }

        return result;
    }

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

    @Override
    public boolean checkUsername(String username) {
        logger.trace("Attempting to check if username {} is taken.", username);

        if (username.isEmpty() || username.isBlank()) {
            logger.warn("Username cannot be empty.");
            return true;
        }

        boolean result = userDAO.checkUsername(username);

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
     * @param user The user.
     */
    private void hashPassword(User user) {
        String salt = passwordHasher.generateSalt();
        String hashed = salt + passwordHasher.hashPassword(user.getPassword() + salt, salt.getBytes());
        user.setPassword(hashed);
    }

    public Map<String, String> read(String username) {
        logger.trace("Fetching user data for user with username {}.", username);

        Map<String, String> result = userDAO.getUserData(username);

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
}
