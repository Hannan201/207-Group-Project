package user;

import behaviors.interfaces.ReadCodeBehavior;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.ArrayList;

/**
 * This class is responsible for storing data
 * related to a specific social media account.
 */

public class Account {

    // Social media type for this account.
    private String socialMediaType;

    // Name for this account (email/username/handle
    // or some sort of identifier).
    private String name;

    // List to store all the backup codes for this
    // specific account.
    private List<String> userCodes;

    // The behavior to determine how the user
    // would like ot enter their codes.
    private ReadCodeBehavior readCodeBehavior;

    private static Map<String, String> icons;

    static {

        // I tried that but something weird happened lol let me try again

        icons = new HashMap<>();

        String discord = Account.class.getClassLoader().getResource("images/icons8-discord-100.png").toExternalForm();
        icons.put("discord", discord);

        String github = Account.class.getClassLoader().getResource("images/icons8-github-100.png").toExternalForm();
        icons.put("github", github);

        String google = Account.class.getClassLoader().getResource("images/icons8-google-100.png").toExternalForm();
        icons.put("google", google);

        String shopify = Account.class.getClassLoader().getResource("images/icons8-shopify-100.png").toExternalForm();
        icons.put("shopify", shopify);
    }

    /**
     * Create a new Social Media Account with
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

    @Override
    public int hashCode() {
        return Objects.hash(getSocialMediaType(), getName());
    }

    /**
     * Overrides the equals method which compares
     * accounts based on their name and socialMediaType to see if they are equal.
     *
     * @return if two accounts are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account comparison = (Account) o;

        return this.name.equalsIgnoreCase(comparison.getName()) &&
                this.socialMediaType.equalsIgnoreCase(comparison.getSocialMediaType());
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
     * [FOR TESTING ONLY]
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

    public static Map<String, String> getIcons() {return icons;}
}
