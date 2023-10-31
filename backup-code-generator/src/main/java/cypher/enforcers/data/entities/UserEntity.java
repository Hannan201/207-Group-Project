package cypher.enforcers.data.entities;

import cypher.enforcers.views.themes.Theme;

/**
 * This class is responsible for storing data
 * related to a user of this application.
 */

public class UserEntity {

    // ID of the user.
    private long id;

    // The user's username.
    private String username;

    // The user's password.
    private String password;

    // Theme for this user.
    private Theme theme = Theme.LIGHT;

    // Login status for this user.
    // 0 = false = logged out.
    // 1 = true = logged in.
    // By default, only time a new user object should be created is
    // when registering, in which case the user should automatically
    // be signed in.
    private short loggedIn = 1;

    /**
     * Creates a user.
     */
    public UserEntity() {

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
     * Set the password for this user.
     *
     * @param newPassword The new password for this user.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Get the password for this user.
     *
     * @return The password for this user.
     */
    public String getPassword() {
        return this.password;
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

    /**
     * Set the login status for the user.
     *
     * @param status True if logged in, false otherwise.
     */
    public void setLoggedIn(boolean status) {
        this.loggedIn = (short) (status ? 1 : 0);
    }

    /**
     * Get the login status for this user.
     *
     * @return True if this user is logged in, false otherwise.
     */
    public boolean getLoggedIn() {
        return loggedIn == 1;
    }

}
