package cypher.enforcers.data.implementations;

import cypher.enforcers.data.entities.UserEntity;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Implementation for the User Data Access Object (DAO) to communicate
 * to the database and make changes to any information related to the users.
 */
public class UserDAOImpl implements UserDAO {

    /** SQLite query to insert a user into the database. */
    private static final String ADD_USER = "INSERT INTO users (username, password) VALUES (?, ?)";

    /**
     * SQLite query to select the most recent user that was added
     * into the database.
     */
    private static final String GET_USER_AFTER_ADDING = "SELECT * FROM users WHERE id = last_insert_rowid()";

    /** SQLite query to update a user in the database. */
    private static final String UPDATE_USER = "UPDATE users SET theme_value = ?, logged_in = ? WHERE id = ?";

    /** SQLite query to select a user by ID from the database. */
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    /** SQLite query to select a user by their username in this database. */
    private static final String GET_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";

    /**
     * SQLite query to select the user that's currently logged in, in this
     * database.
     */
    private static final String GET_LOGGED_IN_USER = "SELECT * FROM users WHERE logged_in = 1";

    /** Logger for the user data access object. */
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    /** Service to communicate to the database. */
    private final DatabaseService databaseService;

    /**
     * Create a new User Data Access Object to load users
     * from a database.
     *
     * @param service The service that provides a connection to the
     *                database.
     */
    public UserDAOImpl(DatabaseService service) {
        this.databaseService = service;
    }

    /**
     * Insert a new user into the database.
     *
     * @param user The user to insert.
     * @return A user object if the user was added, null otherwise.
     */
    @Override
    public UserEntity registerUser(UserEntity user) {
        logger.trace("Now making update to the users table.");

        try {
            databaseService.executeUpdate(ADD_USER, user);

            return databaseService.executeSelect(GET_USER_AFTER_ADDING, UserEntity.class);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }
    }

    /**
     * Get a user from the database with a specific ID.
     *
     * @param id The ID of the user.
     * @return User if found, null otherwise.
     */
    @Override
    public UserEntity getUserByID(long id) {
        logger.trace("Getting user from table with ID {}.", id);

        try {
            return databaseService.executeSelect(GET_USER_BY_ID, UserEntity.class, id);
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
    public UserEntity getUserByName(String username) {
        logger.trace("Getting user from table with username {}.", username);

        try {
            return databaseService.executeSelect(GET_USER_BY_NAME, UserEntity.class, username);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }

    /**
     * Get the data for the user that has a login status of true.
     *
     * @return The user if any user is logged in, null otherwise.
     */
    @Override
    public UserEntity getLoggedInUser() {
        logger.trace("Getting user currently logged in.");

        try {
            return databaseService.executeSelect(GET_LOGGED_IN_USER, UserEntity.class);
        } catch (SQLException e) {
            logger.debug("Failed select query. Cause: ", e);
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
    public UserEntity updateUser(UserEntity user) {
        try {
            int status = user.getLoggedIn() ? 1 : 0;
            databaseService.executeUpdate(UPDATE_USER, user.getTheme().ordinal(), status, user.getID());

            return databaseService.executeSelect(GET_USER_BY_ID, UserEntity.class, user.getID());
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

    }
}