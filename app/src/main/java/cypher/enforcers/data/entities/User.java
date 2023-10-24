package cypher.enforcers.data.entities;

import cypher.enforcers.views.themes.Theme;
import jakarta.persistence.*;

import java.util.List;

/**
 * Java representation of the users table in the database.
 */
@Entity
@Table(name = "users")
public class User {

    // ID for this user.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    // Username for this user.
    @Column(name = "username", nullable = false, unique = true, columnDefinition = "CHECK ( length(username) > 0 )")
    private String username;

    // Hashed password for this user.
    @Column(name = "password", nullable = false, columnDefinition = "CHECK ( length(password) > 0 )")
    private String password;

    // Theme for this user, default is light mode.
    @Enumerated
    @Column(name = "theme_index", nullable = false)
    private Theme theme = Theme.LIGHT;

    // Whether the user is logged in or not.
    // 0 = false = logged out.
    // 1 = true = logged in.
    // By default, when a new user is created/inserted to the table, that
    // means they registered, so the user should be logged in.
    @Column(name = "logged_in", nullable = false, columnDefinition = "CHECK ( logged_in = 0 OR logged_in = 1 )")
    private short loggedIn = 1;

    // Accounts for this user.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;

    /**
     * Create a new user.
     */
    public User() {

    }

    /**
     * Get the ID for this user.
     *
     * @return ID of the user.
     */
    public long getID() {
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
     * Get the hashed password for this user.
     *
     * @return Hashed password for this user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Get the theme for this user.
     *
     * @return The theme for this user.
     */
    public Theme getTheme() {
        return this.theme;
    }

    /**
     * Return the login status for this user.
     *
     * @return 1 if logged in, 0 if logged out.
     */
    public short getLoggedIn() {
        return this.loggedIn;
    }

    /**
     * Check if this user is logged in.
     *
     * @return True if logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return this.loggedIn == 1;
    }

    /**
     * Get the accounts for this user.
     *
     * @return List of accounts for this user.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Set the ID for this user.
     *
     * @param ID New ID for this user.
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Set the username for this user.
     *
     * @param username New username for this user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password for this user.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the theme for this user.
     *
     * @param theme The new theme for this user.
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    /**
     * Set the login status.
     *
     * @param value 1 for logged in, 0 for logged out.
     */
    public void setLoggedIn(short value) {
        if (value != 1 || value != 0) {
            throw new RuntimeException("Invalid value");
        }

        this.loggedIn = value;
    }

    /**
     * Set the list of accounts for this user.
     *
     * @param accounts The list of accounts.
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
