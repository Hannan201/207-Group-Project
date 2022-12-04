package views;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.SwitchToLightMode;
import commands.managers.ThemeSwitcher;
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
            views.add(firstInstance);
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this settings
     * view.
     */
    @Override
    protected void initUI() {
        // make ToggleGroup and ToggleButton
        ToggleButton tb1 = new ToggleButton("HighContrast");
        ToggleButton tb2 = new ToggleButton("Light");
        ToggleButton tb3 = new ToggleButton("Dark");
        ToggleGroup group = new ToggleGroup();
        tb1.setToggleGroup(group);
        tb2.setToggleGroup(group);
        tb2.setSelected(true);
        tb3.setToggleGroup(group);


        // bit implmentation of changing theme

        tb1.setOnAction(e -> {
            this.switcher.setCommand(this.highContrastModeCommand);
            this.switcher.switchTheme();
        });

        tb2.setOnAction(e -> {
            this.switcher.setCommand(this.lightModeCommand);
            this.switcher.switchTheme();
        });

        tb3. setOnAction(e -> {
            this.switcher.setCommand(this.darkModeCommand);
            this.switcher.switchTheme();
        });


        // change Button size
        tb1.setMaxWidth(Double.MAX_VALUE);
        tb2.setMaxWidth(Double.MAX_VALUE);
        tb3.setMaxWidth(Double.MAX_VALUE);

        tb1.setMaxHeight(20);
        tb2.setMaxHeight(20);
        tb3.setMaxHeight(20);

        // create HBox
        HBox hb = new HBox();
        hb.getChildren().addAll(tb1, tb2, tb3);
        hb.setAlignment(Pos.CENTER);
        hb.setMaxWidth(Double.MAX_VALUE);
        // create BoarderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(620);
        borderPane.setPrefWidth(540);
        borderPane.setCenter(hb);

        // create label
        Label heading = new Label("Setting");
        heading.setMaxHeight(Double.MAX_VALUE);
        heading.setMaxWidth(Double.MAX_VALUE);
        heading.setAlignment(Pos.TOP_CENTER);
        borderPane.setTop(heading);


        // create second HBox
        HBox hb2 = new HBox();
        Button deleteAllDataButton = new Button("Delete All Data");
        Button exportDataButton = new Button("Export Data");
        hb2.getChildren().addAll(deleteAllDataButton, exportDataButton);

        // set hb2 to be maximum size
        hb2.setMaxWidth(Double.MAX_VALUE);
        hb2.setMaxWidth(Double.MAX_VALUE);

        // set one button to left. one button to right.
        deleteAllDataButton.setAlignment(Pos.CENTER_LEFT);
        exportDataButton.setAlignment(Pos.CENTER_RIGHT);
        hb2.setAlignment(Pos.CENTER);
        borderPane.setBottom(hb2);
        this.layout = borderPane;
        this.setRoot(layout);

        // functionality of deleting codes and export data

        deleteAllDataButton.setOnAction(e -> {
            throw new UnsupportedOperationException();
        });

        exportDataButton.setOnAction(e -> {
            throw new UnsupportedOperationException();
        });


    }

    /**
     * Switch this settings view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this settings view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this settings view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

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
