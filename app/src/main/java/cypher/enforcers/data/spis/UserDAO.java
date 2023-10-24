package cypher.enforcers.data.spis;

import cypher.enforcers.data.entities.User;
import cypher.enforcers.views.themes.Theme;

import java.util.Optional;

/**
 * Interface for the User Data Access Object (DAO) to communicate to the
 * database and make changes to any information related to the users.
 */
public interface UserDAO {

    /**
     * Check if a given username is taken. The check is case-sensitive.
     *
     * @param username The username to check.
     * @return True if the username is taken, false otherwise.
     */
    boolean checkUsername(String username);

    /**
     * Register a new user.
     *
     * @param user The user to register.
     * @return True if the user was registered successfully, false otherwise.
     */
    boolean registerUser(User user);

    /**
     * Get the current logged-in user.
     *
     * @return An Optional containing the user if found, null otherwise.
     */
    Optional<User> getUser();

    /**
     * Get the theme for the current logged-in user.
     *
     * @return An Optional containing the theme if found, null otherwise.
     */
    Optional<Theme> getTheme();

    /**
     * Update the theme of the current logged-in user.
     *
     * @param newTheme The new theme.
     * @return True if the theme was updated successfully, false otherwise.
     */
    boolean updateTheme(Theme newTheme);
}