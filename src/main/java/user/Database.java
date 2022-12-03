package user;

import java.util.Set;

/**
 * This class is responsible for loading and
 * saving data for users in this application.
 */

public class Database {

    // To check if a user's already logged in.
    private boolean loggedIn;

    // To prevent duplicate usernames.
    private Set<String> usernames;

    // Store current user to load or save data.
    private User user;



}
