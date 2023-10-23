package cypher.enforcers.data.spis;

/**
 * Interface for the Authentication Service. This service allows this
 * application to register, authenticate, and logout users. This way,
 * unauthorised actions are prevented.
 */
public interface AuthenticationService {

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
      * Logout the user from this application that's currently logged in.
      *
      * @return True if the current logged-in user was logged out or if
      * no user is logged-in, false otherwise.
      */
     boolean logUserOut();

     /**
      * Get ID of the user currently logged in.
      *
      * @return ID of the current user.
      */
     long getLoggedInUser();

}
