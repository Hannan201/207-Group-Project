package cypher.enforcers.controllers;

import cypher.enforcers.commands.Command;
import cypher.enforcers.commands.SwitchToDarkMode;
import cypher.enforcers.commands.SwitchToHighContrastMode;
import cypher.enforcers.commands.SwitchToLightMode;
import cypher.enforcers.commands.managers.ThemeSwitcher;
import cypher.enforcers.data.security.dtos.User;
import cypher.enforcers.models.AccountModel;
import cypher.enforcers.models.UserModel;
import cypher.enforcers.views.accountview.AccountView;
import cypher.enforcers.views.codeview.CodeView;
import cypher.enforcers.views.themes.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HyperlinkLabel;
import cypher.enforcers.views.*;
import cypher.enforcers.views.interfaces.Reversible;

import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the settings view.
 */
public class SettingsViewController implements Initializable {

    // Logger for the settings controller.
    private static final Logger logger = LoggerFactory.getLogger(SettingsViewController.class);

    // Used to update the theme of the views.
    private static List<View> views;

    // Contains all the content for the view.
    @FXML
    private VBox mainLayout;

    // Contains the label to hold the copyright text.
    @FXML
    private HyperlinkLabel copyright;

    // Used to cache the commands and theme switcher to avoid
    // creating them again.
    private ThemeSwitcher switcher;

    // Command for light mode.
    private Command lightModeCommand;

    // Command for dark mode.
    private Command darkModeCommand;

    // Command for high contrast mode.
    private Command highContrastModeCommand;

    // The copyright text to display.
    private static final String COPYRIGHT =
            "Special thanks to [Icons8] for the following icons: " +
            "[app] icon, [Google] icon, [Discord]icon, [Shopify] icon, " +
            "[Github] icon, [Settings] icon, [Log Out] icon, [Back Arrow] icon.";

    // Used to interact with the users.
    private UserModel userModel;

    /**
     * Set the user model.
     *
     * @param model The User Model.
     */
    public void setUserModel(UserModel model) {
        this.userModel = model;
    }

    // Used to interact with the accounts.
    private AccountModel accountModel;

    /**
     * Set the account model.
     *
     * @param model The Account Model.
     */
    public void setAccountModel(AccountModel model) {
        this.accountModel = model;
    }

    /**
     * @param url            The location used to resolve relative paths for the root object, or
     *                       {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                       the root object was not localized.
     * @throws UncheckedIOException If any errors occur while loading
     * in all the views for the theme switcher.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) throws UncheckedIOException {
        /*
        The copyright label for some reason would overflow when the
        screen gets to small, and the text would appear on the buttons.
        After trial and error, I pretty much found a break point where
        I increase the minimum height of the label so there's more
        room to prevent overflow.
         */
        mainLayout.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 363) {
                double delta = (363 - newWidth.doubleValue()) / 1.4;
                copyright.setMinHeight(68 + delta);
            } else {
                copyright.setMinHeight(68);
            }
        }));

        copyright.setText(COPYRIGHT);

        views = new ArrayList<>();

        try {
            views.add(SignUpView.getInstance());
            views.add(AccountView.getInstance());
            views.add(AddAccountView.getInstance());
            views.add(CodeView.getInstance());
            views.add(HomePageView.getInstance());
            views.add(SignInView.getInstance());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        lightModeCommand = new SwitchToLightMode(views);
        darkModeCommand = new SwitchToDarkMode(views);
        highContrastModeCommand = new SwitchToHighContrastMode(views);
        switcher = new ThemeSwitcher(lightModeCommand);

        // switch to light mode as default
        this.switcher.switchTheme();
    }

    /**
     * Switch all the view's theme to HighContrastMode
     *
     * @throws IOException if any errors occur while loading in the
     * settings view.
     */
    @FXML
    private void switchToHighContrastMode() throws IOException {
        if (userModel.updateTheme(Theme.HIGH_CONTRAST)) {
            logger.info("Updating application look and feel to high contrast mode.");
            updateTheme(highContrastModeCommand);
        }
    }

    /**
     * Switch all the view's theme to Dark Mode.
     *
     * @throws IOException if any errors occur while loading in the
     * settings view.
     */
    @FXML
    private void switchToDarkMode() throws IOException {
        if (userModel.updateTheme(Theme.DARK)) {
            logger.info("Updating application look and feel to dark mode.");
            updateTheme(darkModeCommand);
        }
    }

    /**
     * Switch all the view's theme to Light Mode.
     *
     * @throws IOException if any errors occur while loading in the
     * settings view.
     */
    @FXML
    private void switchToLightMode() throws IOException {
        if (userModel.updateTheme(Theme.LIGHT)) {
            logger.info("Updating application look and feel to light mode.");
            updateTheme(lightModeCommand);
        }
    }

    /**
     * Update the theme.
     *
     * @param command The command to update
     *                to the correct theme.
     * @throws IOException if any errors occur while loading in the
     * settings view.
     */
    private void updateTheme(Command command) throws IOException {
        this.switcher.setCommand(command);
        this.switcher.switchTheme();
        SettingsView.getInstance().getRoot().getScene().getStylesheets().clear();
        SettingsView.getInstance().getRoot().getScene().getStylesheets().add(SettingsView.getInstance().getCurrentThemePath());

        // This is so the list view updates to display the correct
        // icons for the theme.
        User u = userModel.getCurrentUser();
        if (!Objects.isNull(u)) {
            accountModel.loadAccounts(u.id());
        }
    }

    /**
     * Add a view into the list of views.
     *
     * @param view The new view to be added.
     */
    public static void addView(View view) {
        views.add(view);
    }

    /**
     * Allows a user to logout and redirects them to the home page.
     *
     * @throws IOException if any errors occur while loading in the
     * home page view.
     */
    public void handleLogout() throws IOException {
        if (userModel.logOutUser()) {
            logger.trace("Switching from the SettingsView to the HomePageView.");
            View.switchSceneTo(SettingsView.getInstance(), HomePageView.getInstance());
        }
    }

    /**
     * Allow user to exit the Settings view.
     *
     * @throws IOException if any errors occur while loading the
     * previous view.
     */
    public void handleGoBack() throws IOException {
        logger.trace("Switching to previous view.");
        View.switchSceneTo(SettingsView.getInstance(), ((Reversible) SettingsView.getInstance()).getPreviousView());
    }


    public void handleLinkClick(ActionEvent e) throws IOException {
        Hyperlink link = (Hyperlink) e.getSource();
        final String str = link == null ? "" : link.getText();
        String url = "";
        switch (str) {
            case "Icons8" -> url = "https://icons8.com";
            case "app" -> url = "https://icons8.com/icon/4SBCvFZBi2Rc/app";
            case "Google" -> url = "https://icons8.com/icon/60984/google";
            case "Discord" -> url = "https://icons8.com/icon/30888/discord";
            case "Shopify" -> url = "https://icons8.com/icon/SZ0VDlOvY5zB/shopify";
            case "Github" -> url = "https://icons8.com/icon/62856/github";
            case "Settings" -> url = "https://icons8.com/icon/H6C79JoP90DH/settings";
            case "Back Arrow" -> url = "https://icons8.com/icon/26194/back-arrow";
            case "Log Out" -> url = "https://icons8.com/icon/O78uUJpfEyFx/log-out";
        }

        if (url.isEmpty() || url.isBlank()) {
            logger.warn("Link to icon is empty. Aborting request");
            return;
        }

        logger.debug("Redirecting browser to url: {}.", url);
        Desktop.getDesktop().browse(URI.create(url));
    }

    /**
     * Delete all accounts for this user.
     */
    public void handleDeleteAccounts() {
        User u = userModel.getCurrentUser();
        if (!Objects.isNull(u)) {
            accountModel.clearAllAccounts(u.id());
        }
    }
}
