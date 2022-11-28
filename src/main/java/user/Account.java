package user;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

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

    /**
     * Creat a new Social Media Account with
     * a name and of a specific social media
     * tyoe.
     *
     * @param name Name of social media account.
     * @param type Type of social media.
     */
    public Account(String name, String type) {
        this.name = name;
        this.socialMediaType = type;
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
     * Remove all the backup codes for this social
     * media account.
     */
    public void clearUserCodes() {
        this.userCodes.clear();
    }
}
