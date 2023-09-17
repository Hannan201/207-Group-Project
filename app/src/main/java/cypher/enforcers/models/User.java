package cypher.enforcers.models;

/**
 * This class is responsible for storing data
 * related to a user of this application.
 */

public class User {

    // ID of the user.
    private final int ID;

    // The user's username.
    private final String username;

    // Theme for this user.
    private final String theme;

    /**
     * Create a new user for this application with the
     * username and password passed in as a parameter.
     *
     * @param ID The ID for this user.
     * @param name The username for this user.
     * @param theme The theme for this user.
     */
    public User(int ID, String name, String theme) {
        this.ID = ID;
        this.username = name;
        this.theme = theme;
    }

    /**
     * Get the ID for this user.
     *
     * @return ID for this user.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Get the username for this user.
     *
     * @return Username for this user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the theme for this user.
     *
     * @return Theme for this user.
     */
    public String getTheme() {
        return this.theme;
    }
}
