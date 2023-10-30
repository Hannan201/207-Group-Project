package cypher.enforcers.data.implementations;

import cypher.enforcers.data.security.SecurityUtils;
import cypher.enforcers.data.security.UserDTO;
import cypher.enforcers.data.security.UserDTOMapper;
import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.data.spis.UserRepository;
import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Implementation for the Authentication Service. This service allows this
 * application to register, authenticate, and logout users. This way,
 * unauthorised actions are prevented.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    // Logger for the authentication repository.
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    // Used to store information related to the current logged-in user.
    private final UserRepository userRepository;

    // Used to hide sensitive information.
    private final UserDTOMapper mapper;

    /**
     * Create a new Authentication service linked to a User Repository
     * and a mapper to convert the User object to a transfer object.
     *
     * @param repository The Repository containing the users.
     * @param mapper The mapper that converts a user object to be
     *               transferred.
     */
    public AuthenticationServiceImpl(UserRepository repository, UserDTOMapper mapper) {
        this.userRepository = repository;
        this.mapper = mapper;
    }

    /**
     * Create a new user for this application.
     *
     * @param username Username for the user.
     * @param password Password for the user.
     * @return True if the user was created successfully, false
     * otherwise.
     */
    @Override
    public boolean createUser(String username, String password) {
        String salt = SecurityUtils.createSalt();
        String hashedPassword = salt + SecurityUtils.hashPassword(password + salt, salt.getBytes());

        User user = new User();
        user.setUsername(username.toLowerCase());
        user.setPassword(hashedPassword);

        Optional<User> createdUser = userRepository.create(user);

        if (createdUser.isPresent() && createdUser.get().getLoggedIn() && createdUser.get().getID() >= 1) {
            logger.trace("User created.");
            return true;
        }

        logger.warn("Unable to create user.");
        return false;
    }

    /**
     * Authenticate an existing user into this application.
     *
     * @param username Username for the user.
     * @param password Password for the user.
     * @return True if the user was authenticated successfully, false
     * otherwise.
     */
    @Override
    public boolean authenticateUser(String username, String password) {
        logger.trace("Attempting to authenticate user with username {}.", username);

        Optional<User> userOptional = userRepository.read(username.toLowerCase());
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        if (verifyPassword(user.getPassword(), password)) {
            user.setLoggedIn(true);
            userOptional = userRepository.update(user);

            if (userOptional.isPresent() && userOptional.get().getLoggedIn()) {
                logger.trace("User authenticated.");
                return true;
            } else {
                logger.warn("Failed to authenticate user with username {}.", username);
            }

        } else {
            logger.warn("Failed to authenticate user with username {}.", username);
        }

        return false;
    }

    /**
     * Logout a given user from this application.
     *
     * @param id ID of the user.
     * @return True if the user was logged out or if
     * was not logged in, false otherwise.
     */
    @Override
    public boolean logUserOut(long id) {
        logger.trace("Attempting to log user out.");

        Optional<User> optionalUser = userRepository.read(id);

        if (optionalUser.isEmpty()) {
            logger.warn("User with ID {} does not exist.", id);
            return false;
        }

        User user = optionalUser.get();

        if (!user.getLoggedIn()) {
            logger.trace("Use with ID {} already logged out.", id);
            return true;
        }

        user.setLoggedIn(false);
        optionalUser = userRepository.update(user);

        if (optionalUser.isPresent() && optionalUser.get().getLoggedIn()) {
            logger.warn("Failed to logout user.");
            return false;
        }

        return true;
    }

    /**
     * Update the theme for a given user.
     *
     * @param id The ID of the user.
     * @param theme The new theme of the user.
     * @return True if theme updated successfully, false otherwise.
     */
    @Override
    public boolean updateUserTheme(long id, Theme theme) {
        logger.trace("Attempting to update theme to {} of user with ID {}.", theme, id);

        Optional<User> optionalUser = userRepository.read(id);

        if (optionalUser.isEmpty()) {
            logger.warn("User with ID {} does not exist.", id);
            return false;
        }

        User user = optionalUser.get();

        if (!user.getLoggedIn()) {
            logger.warn("Use with ID {} is not logged in.", id);
            return false;
        }

        user.setTheme(theme);
        optionalUser = userRepository.update(user);

        if (optionalUser.isPresent() && optionalUser.get().getTheme().equals(theme)) {
            logger.trace("Theme successfully updated to {} for user with ID {}.", theme, id);
            return true;
        }

        return false;
    }

    /**
     * Check if a given username is taken.
     *
     * @param username The username to search for.
     * @return True if the username is taken, false otherwise. Note that
     * a blank or empty username is invalid thus this method would return
     * false if that's the case.
     */
    @Override
    public boolean checkUsername(String username) {
        Optional<User> optionalUser = userRepository.read(username.toLowerCase());

        if (optionalUser.isPresent() && optionalUser.get().getUsername().equalsIgnoreCase(username)) {
            logger.debug("Username is taken.");
            return true;
        }

        logger.debug("Username is not taken.");
        return false;
    }

    /**
     * Get the current logged-in user, if any.
     *
     * @return An Optional containing a user if any is logged in, null
     * otherwise.
     */
    @Override
    public Optional<UserDTO> getLoggedInUser() {
        return userRepository.findLoggedInUser()
                .map(user -> mapper.apply(user));
    }

    /**
     * Verify that the password provided by the user matches the
     * one in the database.
     *
     * @param expected The password present in the database.
     * @param actual The password provided by the user.
     * @return True if the passwords match, false otherwise.
     */
    private boolean verifyPassword(String expected, String actual) {
        String salt = expected.substring(0, SecurityUtils.SALT_LENGTH);
        String expectedPassword = expected.substring(SecurityUtils.SALT_LENGTH);

        return SecurityUtils.verifyPassword(expectedPassword, actual + salt, salt);
    }
}
