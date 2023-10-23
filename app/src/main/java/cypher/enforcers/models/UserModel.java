package cypher.enforcers.models;

import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.data.spis.UserRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Optional;

/**
 * This class is used to model a user in this application.
 */
public class UserModel {

    // Used to log-in users.
    private AuthenticationService authSerivce;

    // Used to interact with user objects.
    private UserRepository userRepository;

    /**
     * Create a new user model, loaded with a user if logged in.
     */
    public UserModel() {
        setCurrentUser(userRepository.read().orElse(null));
    }

    // Property to store the current logged in user.
    private final ObjectProperty<User> currentUserProperty = new SimpleObjectProperty<>();

    /**
     * Get the current logged-in user.
     *
     * @return The current user logged-in.
     */
    public User getCurrentUser() {
        return currentUserProperty.get();
    }

    /**
     * Get the property for the current user.
     *
     * @return Property with the current user logged in.
     */
    public ObjectProperty<User> currentUserProperty() {
        return currentUserProperty;
    }

    /**
     * Set the current logged-in user.
     *
     * @param user The new user to be set.
     */
    public void setCurrentUser(User user) {
        currentUserProperty.set(user);
    }

    /**
     * Attempt to log in a user.
     *
     * @param username Username for the user.
     * @param password Password for the user.
     * @return True if successfully logged in, false otherwise.
     */
    public boolean loginUser(String username, String password) {
        boolean result = authSerivce.authenticateUser(username, password);

        if (result) {
            Optional<User> userOptional = userRepository.read();
            userOptional.ifPresentOrElse(
                    this::setCurrentUser,
                    () -> {
                        throw new RuntimeException("User should not be null here.");
                    }
            );
        }

        return result;
    }

    /**
     * Check to see if a username is already taken. The search
     * is case-insensitive.
     *
     * @param username Username to check for.
     * @return True if it is taken, false otherwise.
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.checkUsername(username);
    }

    /**
     * Register a new user.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     * @return True if successfully registered, false otherwise.
     */
    public boolean registerUser(String username, String password) {
        boolean result = userRepository.create(username, password);

        if (result) {
            Optional<User> userOptional = userRepository.read();
            userOptional.ifPresentOrElse(
                    this::setCurrentUser,
                    () -> {
                        throw new RuntimeException("User should not be null here.");
                    }
            );
        }

        return result;
    }

}