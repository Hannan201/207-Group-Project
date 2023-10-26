package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.annotations.SimpleService;

import java.util.Map;

/**
 * Implementation for the Authentication Service. This service allows this
 * application to register, authenticate, and logout users. This way,
 * unauthorised actions are prevented.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    // Logger for the authentication repository.
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    // Used to store information related to the current logged-in user.
    @SimpleService
    private UserRepositoryImpl userRepository;

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

        if (!userRepository.checkUsername(username)) {
            return false;
        }

        Map<String, String> data = userRepository.read(username);

        if (data == null || !(data.containsKey("password") && data.containsKey("id"))) {
            logger.warn("Failed to authenticate user with username {}.", username);
            return false;
        }

        String hashedPassword = data.get("password");
        String salt = hashedPassword.substring(0, 24);
        String expectedPassword = hashedPassword.substring(24);

        boolean loggedIn = userRepository.getPasswordHasher()
                .verifyPassword(
                        expectedPassword,
                        password + salt,
                        salt
                );

        if (loggedIn && userRepository.loginUser(
                Long.parseLong(data.get("id"))
        )) {
            logger.trace("User authenticated.");
        } else {
            logger.warn("Failed to authenticate user with username {}.", username);
        }

        return loggedIn;
    }

    /**
     * Logout the user from this application that's currently logged in.
     *
     * @return True if the current logged-in user was logged out or if
     * no user is logged-in, false otherwise.
     */
    @Override
    public boolean logUserOut() {
        logger.trace("Attempting to log user out.");

        if (!userRepository.logUserOut()) {
            logger.warn("Failed to logout user.");
            return false;
        }

        return true;
    }

    /**
     * Get ID of the user currently logged in.
     *
     * @return ID of the current user.
     */
    public long getLoggedInUser() {
        return userRepository.getLoggedInUser();
    }
}
