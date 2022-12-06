package views;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.SwitchToLightMode;
import commands.managers.ThemeSwitcher;
import controllers.SettingsViewController;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import views.interfaces.Reversible;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for displaying a view
 * to change the settings for this application. Such
 * as the font and theme.
 */

public class SettingsView extends View implements Reversible {

    // An instance for this settings view.
    private static View firstInstance = null;

    // The previous view for this settings view.
    private View previousView;

    private BorderPane layout;
    private static List<View> views;
    private final ThemeSwitcher switcher;
    private final Command lightModeCommand;
    private final Command darkModeCommand;
    private final Command highContrastModeCommand;

    /**
     * Create a new settings view.
     */
    private SettingsView() {
        initUI();
        views = new ArrayList<>(List.of(SignUpView.getInstance(), AccountView.getInstance(),
                AddAccountView.getInstance(), CodeView.getInstance(), HomePageView.getInstance()));
        this.lightModeCommand = new SwitchToLightMode(views);
        this.darkModeCommand = new SwitchToDarkMode(views);
        this.highContrastModeCommand = new SwitchToHighContrastMode(views);
        this.switcher = new ThemeSwitcher(lightModeCommand);

        // switch to light mode as default
        this.switcher.switchTheme();
    }

    /**
     * Return the instance of this settings view.
     *
     * @return Instance of this settings view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SettingsView();
            SettingsViewController.addView(firstInstance);
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this settings
     * view.
     */
    @Override
    protected void initUI() {
        this.names = new String[]{"SettingsView.css",
                "Name of CSS file for dark mode.",
                "HighContrastSettingsView.css"};

        this.loadStylesheets();

        this.loadRoot("SettingsView.fxml");
    }

    /**
     * Get the previous view which was being
     * displayed before this settings-view.
     *
     * @return The previous view for this
     * settings view.
     */
    @Override
    public View getPreviousView() {
        return this.previousView;
    }

    /**
     * Set the previous view for this
     * settings-view to display.
     *
     * @param newPreviousView The new previous
     *                        view for this
     *                        application.
     */
    @Override
    public void setPreviousView(View newPreviousView) {
        this.previousView = newPreviousView;
    }
}
