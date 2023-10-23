package cypher.enforcers.data.spis;

import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;

import java.util.Optional;

/*
 Interface for the User Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public interface UserRepository {

    /**
     * Create a new user. Usernames are case-insensitive.
     *
     * @param username username of the user.
     * @param password password of the user.
     * @return True if the user was successfully created, false
     * otherwise.
     */
    boolean create(String username, String password);

    /**
     * Read the current user logged in.
     *
     * @return An Optional containing the user. Null otherwise.
     */
    Optional<User> read();

    /**
     * Read the theme for the user logged in.
     *
     * @return An Optional containing the theme. Null otherwise.
     */
    Optional<Theme> readTheme();

    /**
     * Update the theme for the user logged in.
     *
     * @return True if the theme was updated, false otherwise.
     */
    boolean update(Theme newTheme);

    /**
     * Check if a given username is taken. The search is
     * case-insensitive.
     *
     * @param username The username to search for.
     * @return True if the user is taken, false otherwise.
     */
    boolean checkUsername(String username);

}
