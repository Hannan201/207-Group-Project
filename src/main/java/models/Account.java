package models;

import behaviors.interfaces.ReadCodeBehavior;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for storing data
 * related to a specific social media account.
 */

public class Account implements java.io.Serializable {

    // Social media type for this account.
    private final String socialMediaType;

    // Name for this account (email/username/handle
    // or some sort of identifier).
    private final String name;

    // List to store all the backup codes for this
    // specific account.
    private final List<String> userCodes;

    // The behavior to determine how the user
    // would like to enter their codes.
    private transient ReadCodeBehavior readCodeBehavior;

    // To load icons for each type of social
    // media account in this application.
    private static final Map<String, String> icons;

    static {
        icons = new HashMap<>();

        URL url;

        url = Account.class.getClassLoader().getResource("images/icons8-discord-100.png");
        if (url != null) {
            String discord = url.toExternalForm();
            icons.put("discord", discord);
        }

        url = Account.class.getClassLoader().getResource("images/icons8-github-100.png");
        if (url != null) {
            String github = url.toExternalForm();
            icons.put("github", github);
        }

        url = Account.class.getClassLoader().getResource("images/icons8-google-100.png");
        if (url != null) {
            String google = url.toExternalForm();
            icons.put("google", google);
        }

        url = Account.class.getClassLoader().getResource("images/icons8-shopify-100.png");
        if (url != null) {
            String shopify = url.toExternalForm();
            icons.put("shopify", shopify);
        }

        url  = Account.class.getClassLoader().getResource("images/icons8-app-100.png");
        if (url != null) {
            String defaultIcon = url.toExternalForm();
            icons.put("default", defaultIcon);
        }
    }

    /**
     * Creat a new Social Media Account with
     * a name and of a specific social media
     * type.
     *
     * @param name Name of social media account.
     * @param type Type of social media.
     */
    public Account(String name, String type) {
        this.name = name;
        this.socialMediaType = type;
        this.userCodes = new ArrayList<>();
    }

    /**
     * Change the behavior for how the codes will
     * be entered for this social media account.
     *
     * @param newBehavior The new behavior to read in the
     *                    codes for this account.
     */
    public void setReadCodeBehavior(ReadCodeBehavior newBehavior) {
        this.readCodeBehavior = newBehavior;
    }

    /**
     * Add codes to this social media account.
     */
    public void addCodes() {
        this.userCodes.addAll(readCodeBehavior.readCodes());
    }

    /**
     * Add codes to this social media account.
     */
    public void addCodes(String code) {
        this.userCodes.add(code);
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
     * Return the backup codes for this
     * social media account.
     *
     * @return List of backup codes.
     */
    public List<String> getUserCodes() {
        return this.userCodes;
    }

    /**
     * Remove all the backup codes for this social
     * media account.
     */
    public void clearUserCodes() {
        this.userCodes.clear();
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
}
