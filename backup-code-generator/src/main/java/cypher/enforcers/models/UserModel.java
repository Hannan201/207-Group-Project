package cypher.enforcers.models;

import cypher.enforcers.data.security.dtos.User;
import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.views.themes.Theme;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;
import java.util.Optional;

/**
 * This class is used to model a user in this application.
 */
public class UserModel {

    // Used to log-in users.
    private final AuthenticationService authService;

    // Run this when the model is being shut down.
    private final Runnable onShutDown;

    /**
     * Create a new user model, loaded with a user if logged in.
     *
     * @param service The Authentication service to verify user
     *                information.
     * @param runnable Function to run when this model is being shut down.
     */
    public UserModel(AuthenticationService service, Runnable runnable) {
        this.authService = service;
        this.onShutDown = runnable;

        setCurrentUser(authService.getLoggedInUser().orElse(null));
    }

    // Property to store the current logged-in user.
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
     * @param user The new User to be set.
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
    public boolean logInUser(String username, String password) {
        boolean result = authService.authenticateUser(username, password);

        if (result) {
            Optional<User> userOptional = authService.getLoggedInUser();
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
     * Attempt to log out the current user.
     *
     * @return True if the user is logged our, or if no user is logged in.
     * False otherwise.
     */
    public boolean logOutUser() {
        User user = getCurrentUser();

        if (!Objects.isNull(user)) {
            boolean result = authService.logUserOut(user.id());
            if (result) {
                setCurrentUser(null);
            }

            return result;
        }

        return false;
    }

    /**
     * Check to see if a username is already taken. The search
     * is case-insensitive.
     *
     * @param username Username to check for.
     * @return True if it is taken, false otherwise.
     */
    public boolean isUsernameTaken(String username) {
        return authService.checkUsername(username);
    }

    /**
     * Register a new user.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     * @return True if successfully registered, false otherwise.
     */
    public boolean registerUser(String username, String password) {
        boolean result = authService.createUser(username, password);

        if (result) {
            Optional<User> userOptional = authService.getLoggedInUser();
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
     * Attempt to update the theme for the current user.
     *
     * @param theme Theme for the new user.
     * @return True if successfully update, false otherwise.
     */
    public boolean updateTheme(Theme theme) {
        if (theme == null) {
            return false;
        }

        User user = getCurrentUser();

        if (!Objects.isNull(user)) {
            return authService.updateUserTheme(user.id(), theme);
        }

        return false;
    }

    /**
     * Shutdown any related services to the user model.
     */
    public void shutDown() {
        onShutDown.run();
    }
}