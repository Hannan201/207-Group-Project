package cypher.enforcers.controllers;

import cypher.enforcers.commands.Command;
import cypher.enforcers.commands.SwitchToDarkMode;
import cypher.enforcers.commands.SwitchToHighContrastMode;
import cypher.enforcers.commands.SwitchToLightMode;
import cypher.enforcers.commands.managers.ThemeSwitcher;
import cypher.enforcers.views.themes.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.data.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HyperlinkLabel;
import cypher.enforcers.data.database.Database;
import cypher.enforcers.views.*;
import cypher.enforcers.views.interfaces.Reversible;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {

    // Logger for the settings controller.
    private static final Logger logger = LoggerFactory.getLogger(SettingsViewController.class);

    private static List<View> views;
    private ThemeSwitcher switcher;

    private Command lightModeCommand;

    private Command darkModeCommand;

    private Command highContrastModeCommand;

    @FXML
    private VBox mainLayout;

    @FXML
    private HyperlinkLabel copyright;

    private static final String COPYRIGHT =
            "Special thanks to [Icons8] for the following icons: " +
            "[app] icon, [Google] icon, [Discord]icon, [Shopify] icon, " +
            "[Github] icon, [Settings] icon, [Log Out] icon, [Back Arrow] icon.";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainLayout.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 363) {
                double delta = (363 - newWidth.doubleValue()) / 1.4;
                copyright.setMinHeight(68 + delta);
            } else {
                copyright.setMinHeight(68);
            }
        }));

        copyright.setText(COPYRIGHT);

        views = new ArrayList<>(
                List.of(
                        SignUpView.getInstance(),
                        AccountView.getInstance(),
                        AddAccountView.getInstance(),
                        CodeView.getInstance(),
                        HomePageView.getInstance(),
                        SignInView.getInstance()
                )
        );

        lightModeCommand = new SwitchToLightMode(views);
        darkModeCommand = new SwitchToDarkMode(views);
        highContrastModeCommand = new SwitchToHighContrastMode(views);
        switcher = new ThemeSwitcher(lightModeCommand);

        // switch to light mode as default
        this.switcher.switchTheme();
    }

    /**
     * Switch all the view's theme to HighContrastMode
     */
    @FXML
    private void switchToHighContrastMode() {
        logger.info("Updating application look and feel to high contrast mode.");
        updateTheme(highContrastModeCommand);

        Database.updateTheme(Storage.getToken(), Theme.HIGH_CONTRAST);
    }

    /**
     * Switch all the view's theme to Dark Mode.
     */
    @FXML
    private void switchToDarkMode() {
        logger.info("Updating application look and feel to dark mode.");
        updateTheme(darkModeCommand);

        Database.updateTheme(Storage.getToken(), Theme.DARK);
    }

    /**
     * Switch all the view's theme to Light Mode.
     *
     */
    @FXML
    private void switchToLightMode() {
        logger.info("Updating application look and feel to light mode.");
        updateTheme(lightModeCommand);

        Database.updateTheme(Storage.getToken(), Theme.LIGHT);
    }

    /**
     * Update the theme.
     *
     * @param command The command to update
     *                to the correct theme.
     */
    private void updateTheme(Command command) {
        this.switcher.setCommand(command);
        this.switcher.switchTheme();
        SettingsView.getInstance().getRoot().getScene().getStylesheets().clear();
        SettingsView.getInstance().getRoot().getScene().getStylesheets().add(SettingsView.getInstance().getCurrentThemePath());

        Utilities.loadAccounts();
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
     */
    public void handleLogout() {
        Database.logUserOut(Storage.getToken());

        logger.trace("Switching from the SettingsView to the HomePageView.");
        View.switchSceneTo(SettingsView.getInstance(), HomePageView.getInstance());

        Storage.setToken(null);
    }

    /**
     * Allow user to exit the Settings view.
     */
    public void handleGoBack() {
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
        Database.clearAllAccounts(Storage.getToken());
        Utilities.loadAccounts();
    }
}
