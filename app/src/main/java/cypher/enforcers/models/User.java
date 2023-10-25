package cypher.enforcers.models;

import cypher.enforcers.views.themes.Theme;

/**
 * This class is responsible for storing data
 * related to a user of this application.
 */

public class User {

    // ID of the user.
    private long id;

    // The user's username.
    private String username;

    // Theme for this user.
    private Theme theme;

    /**
     * Create a new user for this application with the
     * username and password passed in as a parameter.
     *
     * @param id The ID for this user.
     * @param name The username for this user.
     * @param theme The theme for this user.
     */
    public User(long id, String name, Theme theme) {
        this.id = id;
        this.username = name;
        this.theme = theme;
    }

    /**
     * Get the ID for this user.
     *
     * @return ID for this user.
     */
    public long getID() {
        return this.id;
    }

    /**
     * Set the ID for this user.
     *
     * @param newId New ID for this user.
     */
    public void setID(long newId) {
        this.id = newId;
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
     * Set the username for this user.
     *
     * @param newUsername The new username.
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Get the theme for this user.
     *
     * @return Theme for this user.
     */
    public Theme getTheme() {
        return this.theme;
    }

    /**
     * Srt the theme for this user.
     *
     * @param newTheme The new theme.
     */
    public void setTheme(Theme newTheme) {
        if (newTheme == null) {
            throw new RuntimeException("Theme cannot be null.");
        }

        this.theme = newTheme;
    }

}
