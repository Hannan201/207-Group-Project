package user;

import behaviors.interfaces.ReadCodeBehavior;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static Map<String, ImageView> icons;

    static {

        // I tried that but something weird happened lol let me try again

        icons = new HashMap<>();

        String discord = Account.class.getClassLoader().getResource("images/icons8-discord-100.png").toExternalForm();
        ImageView discord_image = new ImageView(discord);
        icons.put("discord", discord_image);

        String github = Account.class.getClassLoader().getResource("images/icons8-github-100.png").toExternalForm();
        ImageView github_image = new ImageView(github);
        icons.put("github", github_image);

        String google = Account.class.getClassLoader().getResource("images/icons8-google-100.png").toExternalForm();
        ImageView google_image = new ImageView(google);
        icons.put("google", google_image);

        String shopify = Account.class.getClassLoader().getResource("images/icons8-shopify-100.png").toExternalForm();
        ImageView shopify_image = new ImageView(shopify);
        icons.put("shopify", shopify_image);
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

    public static Map<String, ImageView> getIcons() {return icons;}
}
