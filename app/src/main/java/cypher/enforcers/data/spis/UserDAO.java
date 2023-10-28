package cypher.enforcers.data.spis;

import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;

/**
 * Interface for the User Data Access Object (DAO) to communicate to the
 * database and make changes to any information related to the users.
 */
public interface UserDAO {

    /**
     * Insert a new user into the database,
     *
     * @param user The user to insert.
     * @return A user object if the user was added, null otherwise.
     */
    User registerUser(User user);

    /**
     * Get a user from the database with a specific ID.
     *
     * @param id The ID of the user.
     * @return User if found, null otherwise.
     */
    User getUserByID(long id);

    /**
     * Get a user from the database with a specific username.
     *
     * @param username The username of the user.
     * @return User if found, null otherwise.
     */
    User getUserByName(String username);

    /**
     * Update a user in the database.
     *
     * @param user The user to update.
     * @return User object with the updated data if successfully found,
     * null otherwise.
     */
    User updateUser(User user);
}