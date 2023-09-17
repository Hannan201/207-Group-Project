package cypher.enforcers.models;

import cypher.enforcers.utilities.Utilities;

import java.util.*;

/**
 * This class is responsible for storing data
 * related to a specific social media account.
 */

public class Account {

    // List of social media platforms that allow the imports
    // of text files.
    private static final Set<String> SUPPORTED_IMPORT_PLATFORMS
            = Set.copyOf(List.of("shopify", "discord", "google", "github"));

    // ID for this account.
    private final int ID;

    // Name for this account (email/username/handle
    // or some sort of identifier).
    private final String name;

    // Social media type for this account.
    private final String socialMediaType;

    // To load icons for each type of social
    // media account in this application.
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
     * Create a new Social Media Account with
     * a name and of a specific social media
     * type.
     *
     * @param ID ID for this account.
     * @param name Name of social media account.
     * @param type Type of social media.
     */
    public Account(int ID, String name, String type) {
        this.ID = ID;
        this.name = name;
        this.socialMediaType = type;
    }

    public int getID() {
        return this.ID;
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
     * Return what social media this account belongs to.
     *
     * @return The social media type for this application.
     */
    public String getSocialMediaType() {
        return socialMediaType;
    }

    /**
     * Get the icons for this application which
     * are used when showing the accounts.
     *
     * @return A Map where the key is the
     * name of the platform (in lowercase)
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

        Account account = (Account) o;

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
