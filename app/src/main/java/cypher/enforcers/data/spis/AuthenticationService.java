package cypher.enforcers.data.spis;

import cypher.enforcers.data.security.UserDTO;
import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;

import java.util.Optional;

/**
 * Interface for the Authentication Service. This service allows this
 * application to register, authenticate, and logout users. This way,
 * unauthorised actions are prevented.
 */
public interface AuthenticationService {

     /**
      * Create a new user for this application.
      *
      * @param username Username for the user.
      * @param password Password for the user.
      * @return True if the user was created successfully, false
      * otherwise.
      */
     boolean createUser(String username, String password);

     /**
      * Authenticate an existing user into this application.
      *
      * @param username Username for the user.
      * @param password Password for the user.
      * @return True if the user was authenticated successfully, false
      * otherwise.
      */
     boolean authenticateUser(String username, String password);

     /**
      * Logout a given user from this application.
      *
      * @param id ID of the user.
      * @return True if the user was logged out or if
      * was not logged in, false otherwise.
      */
     boolean logUserOut(long id);

     /**
      * Update the theme for a given user.
      *
      * @param id The ID of the user.
      * @param theme The new theme of the user.
      * @return True if theme updated successfully, false otherwise.
      */
     boolean updateUserTheme(long id, Theme theme);

     /**
      * Check if a given username is taken.
      *
      * @param username The username to search for.
      * @return True if the username is taken, false otherwise. Note that
      * a blank or empty username is invalid thus this method would return
      * false if that's the case.
      */
     boolean checkUsername(String username);

     /**
      * Get the current logged-in user, if any.
      *
      * @return An Optional containing a user if any is logged in, null
      * otherwise.
      */
     Optional<UserDTO> getLoggedInUser();

}
