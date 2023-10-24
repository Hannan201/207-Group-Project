package cypher.enforcers.data.entities;

import jakarta.persistence.*;

import java.util.List;

/**
 * Java representation of the accounts table in the database.
 */
@Entity
@Table(name = "accounts")
public class Account {

    // ID of the account.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    // Name of the account.
    @Column(name = "name", nullable = false, columnDefinition = "CHECK ( length(name) > 0 )")
    private String name;

    // Social Media type of the account.
    @Column(name = "type", nullable = false, columnDefinition = "CHECK ( length(type) > 0 )")
    private String socialMediaType;

    // User associated with this account.
    @ManyToOne(optional = false)
    private User user;

    // Codes associated with this account.
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Code> codes;

    /**
     * Create a new account.
     */
    public Account() {

    }

    /**
     * Get the ID of this account.
     *
     * @return The ID of the account.
     */
    public long getID() {
        return this.ID;
    }

    /**
     * Get the name of this account.
     *
     * @return The name of this account.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the social media type of this account.
     *
     * @return Social media type of this account.
     */
    public String getSocialMediaType() {
        return socialMediaType;
    }

    /**
     * Get the user this account belongs to.
     *
     * @return User of this account.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the ID of this account.
     *
     * @param ID New ID for this account.
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Set the name for this account.
     *
     * @param name New name for this account.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the social media type for this account.
     *
     * @param socialMediaType The new social media type.
     */
    public void setSocialMediaType(String socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    /**
     * Set the user for this account.
     *
     * @param user The new user for this account.
     */
    public void setUser(User user) {
        this.user = user;
    }
}