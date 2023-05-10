package controllers;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.SwitchToLightMode;
import commands.managers.ThemeSwitcher;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HyperlinkLabel;
import data.Database;
import user.User;
import views.*;
import views.interfaces.Reversible;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    private static List<View> views;
    private ThemeSwitcher switcher;

    private Command lightModeCommand;

    private Command darkModeCommand;

    private Command highContrastModeCommand;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private Label label;

    @FXML
    private VBox centerLayout;

    @FXML
    private HBox themeButtons;

    @FXML
    private ToggleButton highContrastModeButton;

    @FXML
    private ToggleButton darkModeButton;

    @FXML
    private ToggleButton lightModeButton;

    @FXML
    private HBox dataRow;

    @FXML
    private Button deleteAccount;

    @FXML
    private Button exportData;

    @FXML
    private HyperlinkLabel copyright;

    @FXML
    private HBox bottomRow;

    @FXML
    private Button back;

    @FXML
    private Button logout;

    private static final String COPYRIGHT =

            "Special thanks to [Icons8] for the following icons: " +
            "[app] icon, [Google] icon, [Discord]icon, [Shopify] icon, " +
            "[Github] icon, [Settings] icon, [Log Out] icon, [Back Arrow] icon.";

    // Don't want the font to be too large.
    private final int MAX_FONT_SIZE = 30;

    // To change the font size of the title.
    private final ObjectProperty<Font> titleFontTracking = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the font size needed, of the title.
    private final DoubleProperty titleFontSize = new SimpleDoubleProperty();

    private final ObjectProperty<Font> baseFont = new SimpleObjectProperty<>(Font.getDefault());

    private final ObjectProperty<Insets> highContrastModePaddingSize = new SimpleObjectProperty<>(new Insets(22, 10, 23, 10));

    private final ObjectProperty<Insets> darkModePaddingSize = new SimpleObjectProperty<>(new Insets(22, 40, 23, 41));

    private final ObjectProperty<Insets> lightModePaddingSize = new SimpleObjectProperty<>(new Insets(22, 60, 23, 61));

    private final ObjectProperty<Insets> exportPaddingSize = new SimpleObjectProperty<>(new Insets(5, 20, 5, 21));

    private final DoubleProperty copyrightTextSize = new SimpleDoubleProperty(15);

    private final ObjectProperty<Insets> windowPadding = new SimpleObjectProperty<>(new Insets(24, 37.5, 12, 37.5));

    private final ObjectProperty<Insets> bottomButtonsPadding = new SimpleObjectProperty<>(new Insets(7, 17, 7, 17.5));

    private final DoubleProperty bottomRowSpacing = new SimpleDoubleProperty(170);

    private final DoubleProperty centerSpacing = new SimpleDoubleProperty(22);

    private final ObjectProperty<Insets> centerLayoutPadding = new SimpleObjectProperty<>(new Insets(20, 0, 0, 0));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        highContrastModeCommand = new SwitchToHighContrastMode(
                views
        );
        switcher = new ThemeSwitcher(lightModeCommand);

        // switch to light mode as default
        this.switcher.switchTheme();

        titleFontSize.bind(
                mainLayout.widthProperty()
                          .add(
                            mainLayout.heightProperty()
                          )
                          .divide(1280 + 720)
                          .multiply(100)
                          .multiply(30.0 / 58.0)
        );

        mainLayout.widthProperty().addListener((observableValue, number, t1) -> {
            double result = Math.min(
                    MAX_FONT_SIZE,
                    titleFontSize.getValue()
            );
            titleFontTracking.set(Font.font(result));

            highContrastModePaddingSize.set(
                    new Insets(
                            result * (22.0 / 30.0),
                            result * (1.0 / 3.0),
                            result * (23.0 / 30.0),
                            result * (1.0 / 3.0)
                    )
            );

            darkModePaddingSize.set(
                    new Insets(
                            result * (22.0 / 30.0),
                            result * (40.0 / 30.0),
                            result * (23.0 / 30.0),
                            result * (41.0 / 30.0)
                    )
            );

            lightModePaddingSize.set(
                    new Insets(
                            result * (22.0 / 30.0),
                            result * 2,
                            result * (23.0 / 30.0),
                            result * (61.0 / 30.0)
                    )
            );

            exportPaddingSize.set(
                    new Insets(
                            result * (5.0 / 30.0),
                            result * (20.0 / 30.0),
                            result * (5.0 / 30.0),
                            result * 0.7
                    )
            );
            baseFont.set(Font.font(result / 2.0));
            windowPadding.set(
                    new Insets(
                            result * 0.8,
                            result * 1.25,
                            result * 0.4,
                            result * 1.25
                    )
            );
            bottomButtonsPadding.set(
                    new Insets(
                            result * (7.0 / 30.0),
                            result * (17.0 / 30.0),
                            result * (7.0 / 30.0),
                            result * (17.5 / 30.0)
                    )
            );
            bottomRowSpacing.set(result * (170.0 / 30.0));
            centerLayoutPadding.set(
                    new Insets(
                            result * (2.0 / 3.0),
                            0,
                            0,
                            0
                    )
            );
            copyrightTextSize.set(result / 2.0);
            centerSpacing.set(result * (22.0 / 30.0));
            VBox.setMargin(
                    themeButtons,
                    new Insets(
                            0,
                            0,
                            result * 0.8,
                            0
                    )
            );
        });

        mainLayout.heightProperty().addListener((observableValue, number, t1) -> {
            double result = Math.min(
                    MAX_FONT_SIZE,
                    titleFontSize.getValue()
            );
            titleFontTracking.set(Font.font(result));

            highContrastModePaddingSize.set(
                    new Insets(
                            result * (22.0 / 30.0),
                            result * (1.0 / 3.0),
                            result * (23.0 / 30.0),
                            result * (1.0 / 3.0)
                    )
            );

            darkModePaddingSize.set(
                    new Insets(
                            result * (22.0 / 30.0),
                            result * (40.0 / 30.0),
                            result * (23.0 / 30.0),
                            result * (41.0 / 30.0)
                    )
            );

            lightModePaddingSize.set(
                    new Insets(
                            result * (22.0 / 30.0),
                            result * 2,
                            result * (23.0 / 30.0),
                            result * (61.0 / 30.0)
                    )
            );

            exportPaddingSize.set(
                    new Insets(
                            result * (5.0 / 30.0),
                            result * (20.0 / 30.0),
                            result * (5.0 / 30.0),
                            result * 0.7
                    )
            );
            baseFont.set(Font.font(result / 2.0));
            windowPadding.set(
                    new Insets(
                            result * 0.8,
                            result * 1.25,
                            result * 0.4,
                            result * 1.25
                    )
            );
            bottomButtonsPadding.set(
                    new Insets(
                            result * (7.0 / 30.0),
                            result * (17.0 / 30.0),
                            result * (7.0 / 30.0),
                            result * (17.5 / 30.0)
                    )
            );
            bottomRowSpacing.set(result * (170.0 / 30.0));
            centerLayoutPadding.set(
                    new Insets(
                            result * (2.0 / 3.0),
                            0,
                            0,
                            0
                    )
            );
            copyrightTextSize.set(result / 2.0);
            centerSpacing.set(result * (22.0 / 30.0));
            VBox.setMargin(
                    themeButtons,
                    new Insets(
                            0,
                            0,
                            result * 0.8,
                            0
                    )
            );
        });

        mainLayout.paddingProperty().bind(windowPadding);

        centerLayout.paddingProperty().bind(centerLayoutPadding);
        centerLayout.spacingProperty().bind(centerSpacing);

        label.fontProperty().bind(titleFontTracking);

        highContrastModeButton.paddingProperty().bind(
                highContrastModePaddingSize
        );
        highContrastModeButton.fontProperty().bind(baseFont);

        darkModeButton.paddingProperty().bind(
                darkModePaddingSize
        );
        darkModeButton.fontProperty().bind(baseFont);

        lightModeButton.paddingProperty().bind(
                lightModePaddingSize
        );
        lightModeButton.fontProperty().bind(baseFont);

        Platform.runLater(() -> {
            dataRow.spacingProperty().bind(
                    highContrastModeButton.widthProperty()
                            .add(
                                darkModeButton.widthProperty()
                            )
                            .add(lightModeButton.widthProperty())
                            .subtract(exportData.widthProperty())
                            .subtract(deleteAccount.widthProperty())
            );
        });

        exportData.paddingProperty().bind(
                exportPaddingSize
        );
        exportData.fontProperty().bind(baseFont);

        deleteAccount.fontProperty().bind(baseFont);

        copyright.styleProperty().bind(
                Bindings.concat(
                        "-fx-font-size: ",
                        copyrightTextSize.asString(),
                        "px;"
                )
        );

        back.paddingProperty().bind(bottomButtonsPadding);
        logout.paddingProperty().bind(bottomButtonsPadding);

        bottomRow.spacingProperty().bind(bottomRowSpacing);

        copyright.prefWidthProperty().bind(
                highContrastModeButton.widthProperty()
                        .add(darkModeButton.widthProperty())
                        .add(lightModeButton.widthProperty())
        );
    }

    /**
     * Switch all the view's theme to HighContrastMode
     *
     */
    @FXML
    private void switchToHighContrastMode() {
        Database.setCurrentTheme("High Contrast");
        updateTheme(highContrastModeCommand);
    }

    /**
     * Switch all the view's theme to Dark Mode.
     */
    @FXML
    private void switchToDarkMode() {
        Database.setCurrentTheme("Dark");
        updateTheme(darkModeCommand);
    }

    /**
     * Switch all the view's theme to Light Mode.
     *
     */
    @FXML
    private void switchToLightMode() {
        Database.setCurrentTheme("Light");
        updateTheme(lightModeCommand);
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

        User user = Database.getUser();
        if (user != null ) {
            ((AccountView) AccountView.getInstance()).getAccountViewController().addAccounts(user.getAccounts());
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
     */
    public void handleLogout() {
        Database.logUserOut();
        View.switchSceneTo(SettingsView.getInstance(), HomePageView.getInstance());
    }

    /**
     * Allow user to exit the Settings view.
     */
    public void handleGoBack() {
        View.switchSceneTo(SettingsView.getInstance(), ((Reversible) SettingsView.getInstance()).getPreviousView());
    }


    public void handleLinkClick(ActionEvent e) throws IOException, URISyntaxException {
        Hyperlink link = (Hyperlink) e.getSource();
        final String str = link == null ? "" : link.getText();
        switch (str) {
            case "Icons8" -> {
                String url = "https://icons8.com";
                Desktop.getDesktop().browse(new URL(url).toURI());
            }
            case "app" -> {
                String app = "https://icons8.com/icon/4SBCvFZBi2Rc/app";
                Desktop.getDesktop().browse(new URL(app).toURI());
            }
            case "Google" -> {
                String google = "https://icons8.com/icon/60984/google";
                Desktop.getDesktop().browse(new URL(google).toURI());
            }
            case "Discord" -> {
                String discord = "https://icons8.com/icon/30888/discord";
                Desktop.getDesktop().browse(new URL(discord).toURI());
            }
            case "Shopify" -> {
                String shopify = "https://icons8.com/icon/SZ0VDlOvY5zB/shopify";
                Desktop.getDesktop().browse(new URL(shopify).toURI());
            }
            case "Github" -> {
                String github = "https://icons8.com/icon/62856/github";
                Desktop.getDesktop().browse(new URL(github).toURI());
            }
            case "Settings" -> {
                String settings = "https://icons8.com/icon/H6C79JoP90DH/settings";
                Desktop.getDesktop().browse(new URL(settings).toURI());
            }
            case "Back Arrow" -> {
                String log_out = "https://icons8.com/icon/26194/back-arrow";
                Desktop.getDesktop().browse(new URL(log_out).toURI());
            }
            case "Log Out" -> {
                String back_arrow = "https://icons8.com/icon/O78uUJpfEyFx/log-out";
                Desktop.getDesktop().browse(new URL(back_arrow).toURI());
            }
        }
    }

    /**
     * Delete all accounts for this user.
     */
    public void handleDeleteAccounts() {
        User user = Database.getUser();
        if (user != null) {
            Database.clearUserData();
            ((AccountView) AccountView.getInstance()).getAccountViewController().addAccounts(user.getAccounts());
        }
    }
}
