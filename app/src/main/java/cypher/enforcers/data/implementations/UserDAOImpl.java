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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation for the User Data Access Object (DAO) to communicate
 * to the database and make changes to any information related to the users.
 */
public class UserDAOImpl implements UserDAO {

    private static final String ADD_USER = "INSERT INTO users (username, password) VALUES (?, ?)";

    private static final String UPDATE_USER = "UPDATE users SET theme_value = ?, logged_in = ? WHERE id = ?";

    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String GET_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";


    // Logger for the user data access object.
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    // Service to communicate to the database.
    @SimpleService
    private DatabaseService databaseService;

    /**
     * Insert a new user into the database,
     *
     * @param user The user to insert.
     * @return A user object if the user was added, null otherwise.
     */
    @Override
    public User registerUser(User user) {
        logger.trace("Now making update to the users table.");

        try {
            databaseService.executeUpdate(ADD_USER, user);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return user;
    }

    /**
     * Get a user from the database with a specific ID.
     *
     * @param id The ID of the user.
     * @return User if found, null otherwise.
     */
    @Override
    public User getUserByID(long id) {
        logger.trace("Getting user from table with ID {}.", id);

        try {
            return databaseService.executeSelect(GET_USER_BY_ID, User.class, id);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }

    /**
     * Get a user from the database with a specific username.
     *
     * @param username The username of the user.
     * @return User if found, null otherwise.
     */
    @Override
    public User getUserByName(String username) {
        logger.trace("Getting user from table with username {}.", username);

        try {
            return databaseService.executeSelect(GET_USER_BY_NAME, User.class, username);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }

    /**
     * Update a user in the database.
     *
     * @param user The user to update.
     * @return User object with the updated data if successfully found,
     * null otherwise.
     */
    @Override
    public User updateUser(User user) {
        try {
            short status = (short) (user.getLoggedIn() ? 1 : 0);
            databaseService.executeUpdate(UPDATE_USER, user.getTheme().ordinal(), status, user.getID());
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return user;
    }
}