package cypher.enforcers.data.entities;

import cypher.enforcers.utilities.Utilities;

import java.util.*;

/**
 * This class is responsible for storing data
 * related to a specific social media account.
 */
public class AccountEntity {

    /**
     * List of social media platforms that allow the imports
     * of text files.
     */
    private static final Set<String> SUPPORTED_IMPORT_PLATFORMS
            = Set.copyOf(List.of("shopify", "discord", "google", "github"));

    /** ID for this account. */
    private long id;

    /** ID of the user this account belongs to. */
    private long userId;

    /**
     * Name for this account (email/username/handle
     * or some sort of identifier).
     */
    private String name;

    /** Social media type for this account. */
    private String socialMediaType;

    /**
     * To load icons for each type of social
     * media account in this application.
     */
    private static final Map<String, String> icons;

    static {
        icons = new HashMap<>();
        Utilities.updateIcons(
                icons,
                "images/icons8-discord-100.png",
                "images/icons8-github-100.png",
                "images/icons8-google-100.png",
                "images/icons8-shopify-100.png",
                "images/icons8-app-100.png"
        );
    }

    /**
     * Create an account.
     */
    public AccountEntity() {

    }

    /**
     * Get the ID of this account.
     *
     * @return The ID of this account.
     */
    public long getID() {
        return this.id;
    }

    /**
     * Set the ID for this account.
     *
     * @param id ID of this account.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the ID of the user this account belongs to.
     *
     * @return The ID of the user.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Set the ID of the user this account belongs to.
     *
     * @param userId The new ID.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Return name of this social media account.
     *
     * @return The name of this social media account.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name for this account.
     *
     * @param newName The new name.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Return what social media this account belongs to.
     *
     * @return The social media type for this application.
     */
    public String getSocialMediaType() {
        return socialMediaType;
    }

    /**
     * Set the Social Media Type of this account.
     *
     * @param socialMediaType The new social media type.
     */
    public void setSocialMediaType(String socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    /**
     * Get the icons for this application which
     * are used when showing the accounts.
     *
     * @return A Map where the key is the
     * name of the platform (in lowercase),
     * and the value is the path to the file.
     */
    public static Map<String, String> getIcons() {
        return icons;
    }

    /**
     * Check if this account is equal to
     * another account. Two accounts are equal
     * if they have the same name and social
     * media platform.
     *
     * @param o The Account to compare with.
     * @return True if equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity account = (AccountEntity) o;

        if (!socialMediaType.equals(account.socialMediaType)) return false;
        return name.equals(account.name);
    }

    /**
     * Produce hash-code for this object.
     *
     * @return The HashCode.
     */
    @Override
    public int hashCode() {
        int result = socialMediaType.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    /**
     * Check if a given social media platform is supported by this
     * application to import codes by using a text file.
     *
     * @param socialMediaType Social media type.
     * @return True if supported, false otherwise.
     */
    public static boolean supportsImport(String socialMediaType) {
        return SUPPORTED_IMPORT_PLATFORMS.contains(socialMediaType.toLowerCase());
    }
}
