package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.UserDAO;
import cypher.enforcers.data.spis.UserRepository;
import cypher.enforcers.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation for the User Data Access Object (DAO) to communicate to
 * the database and make changes to any information related to the users.
 */
public class UserRepositoryImpl implements UserRepository {

    // Logger for the user repository.
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    // To make updates to the database.
    private final UserDAO userDAO;

    /**
     * Create a new User Repository linked to a User Data Access Object
     * to work with the users as a collection.
     *
     * @param dao The User Data Access Object.
     */
    public UserRepositoryImpl(UserDAO dao) {
        this.userDAO = dao;
    }

    /**
     * Create a new user. Usernames are case-insensitive.
     *
     * @param user The user to create.
     * @return An optional containing the user if created successfully,
     * null otherwise.
     */
    @Override
    public Optional<User> create(User user) {
        logger.trace("Attempting to create user with username {}.", user.getUsername());

        if (user.getUsername().isBlank() || user.getUsername().isEmpty()
            || user.getPassword().isBlank() || user.getPassword().isEmpty()) {
            logger.warn("Username or password cannot be empty.");
            return Optional.empty();
        }

        User createUser = userDAO.registerUser(user);

        if (!Objects.isNull(user)) {
            logger.trace("User created.");
            return Optional.of(createUser);
        }

        logger.warn("Failed to create user with username {}.", user.getUsername());
        return Optional.empty();
    }

    /**
     * Read a user by its ID.
     *
     * @param id The ID of the user.
     * @return An Optional containing the user if found, null otherwise.
     */
    @Override
    public Optional<User> read(long id) {
        logger.trace("Attempting to get user with ID {}.", id);

        User user = userDAO.getUserByID(id);

        if (!Objects.isNull(user)) {
            logger.trace("User found.");
            return Optional.of(user);
        }

        logger.warn("Unable to get user with ID {}.", id);
        return Optional.empty();
    }

    /**
     * Read a user by its username. The search is case-insensitive.
     *
     * @param username The username to search for.
     * @return An Optional containing the user if found, null otherwise.
     */
    @Override
    public Optional<User> read(String username) {
        logger.trace("Fetching user with username {}.", username);

        User user = userDAO.getUserByName(username);

        if (!Objects.isNull(user)) {
            logger.trace("User found.");
            return Optional.of(user);
        }

        logger.trace("Unable to find user with username {}.", username);
        return Optional.empty();
    }

    /**
     * Find the current user that's logged in.
     *
     * @return An Optional containing the user if found, null otherwise.
     */
    @Override
    public Optional<User> findLoggedInUser() {
        logger.trace("Attempting to find logged in user.");

        User user = userDAO.getLoggedInUser();

        if (!Objects.isNull(user)) {
            logger.trace("User found.");
            return Optional.of(user);
        }

        logger.trace("No user currently logged in.");
        return Optional.empty();
    }

    /**
     * Update a user.
     *
     * @return An Optional containing the user if updated, null otherwise.
     */
    @Override
    public Optional<User> update(User user) {
        logger.trace("Attempting to update user with ID {}.", user.getID());

        User updatedUser = userDAO.updateUser(user);

        if (!Objects.isNull(updatedUser)) {
            logger.trace("User updated successfully.");
            return Optional.of(updatedUser);
        }

        logger.warn("Unable to update user with ID {}.", user.getID());
        return Optional.empty();
    }

}