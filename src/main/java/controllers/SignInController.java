package controllers;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.managers.ThemeSwitcher;
import data.Database;
import effects.HoverEffect;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.util.Duration;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import themes.Theme;
import transitions.BorderFillTransition;
import transitions.LinearGradientFillTransition;
import transitions.TextFillTransition;
import user.User;
import views.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A controller for the Sign-in UI
 */
public class SignInController implements Initializable {

    private final Validator validator = new Validator();

    /**
     * Background of the scene
     */
    @FXML
    private VBox background;
    /**
     * Title of the scene
     */
    @FXML
    private Label Title;

    @FXML
    private HBox usernameRow;

    @FXML
    private Label Username;

    /**
     * TextField for the user to type their username
     */
    @FXML
    private TextField unameInput;

    @FXML
    private HBox passwordRow;

    @FXML
    private Label Password;

    /**
     * TextField for the user to type their password
     */
    @FXML
    private TextField passInput;

    /**
     * Sign in button that triggers the sign in event
     */
    @FXML
    private Button signInButton;

    // To store all the views.
    private static List<View> views;

    // Don't want the font to be too large.
    private final int MAX_FONT_SIZE = 50;

    // To change the font size of the title.
    private final ObjectProperty<Font> titleFontTracking = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the font size needed, of the title.
    private final DoubleProperty titleFontSize = new SimpleDoubleProperty();

    // To dynamically calculate the font size needed, of the labels.
    private final ObjectProperty<Font> labelFontSize = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the width/height size needed,
    // of the password and username text fields.
    private final DoubleProperty fieldWidthSize = new SimpleDoubleProperty();
    private final DoubleProperty fieldHeightSize = new SimpleDoubleProperty();

    // To dynamically calculate the padding needed, of the button
    // based on the font size.
    private final ObjectProperty<Insets> buttonPaddingSize = new SimpleObjectProperty<>(new Insets(5, 26, 5, 26));

    // To dynamically calculate the spacing needed, of the rows
    // based on the font size.
    private final DoubleProperty usernameRowSpacing = new SimpleDoubleProperty(19);
    private final DoubleProperty passwordRowSpacing = new SimpleDoubleProperty(23);

    private final DoubleProperty spacing =
            new SimpleDoubleProperty(12);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        newWindow.setOnCloseRequest((windowEvent -> clearFields()));
                    }
                }));
            }
        }));

        views = new ArrayList<>(List.of(HomePageView.getInstance(), SignUpView.getInstance(),
                    AccountView.getInstance(), AddAccountView.getInstance(),
                    CodeView.getInstance()));

        signInButton = new Button("Sign In");
        signInButton.setPrefSize(Button.USE_COMPUTED_SIZE, Button.USE_COMPUTED_SIZE);
        signInButton.setAlignment(Pos.CENTER);
        signInButton.paddingProperty().bind(buttonPaddingSize);

        signInButton.setTextFill(Theme.getConfiguration()
                .getPrimaryText()
                .getStart());

        signInButton.setBackground(
                new Background(
                        new BackgroundFill(
                                Theme.getConfiguration()
                                        .getPrimaryBackground()
                                        .getStart(),
                                new CornerRadii(4),
                                Insets.EMPTY
                        )
                )
        );

        signInButton.setBorder(
                new Border(
                        new BorderStroke(
                                Theme.getConfiguration()
                                        .getPrimaryBorder()
                                        .getStart(),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(4),
                                BorderWidths.DEFAULT
                        )
                )
        );

        unameInput.setBackground(
                new Background(
                        new BackgroundFill(
                                Theme.getConfiguration()
                                        .getTertiaryBackground()
                                        .getStart(),
                                new CornerRadii(3),
                                Insets.EMPTY
                        )
                )
        );

        unameInput.setBorder(
                new Border(
                        new BorderStroke(
                                Theme.getConfiguration()
                                        .getTertiaryBorder()
                                        .getStart(),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(2),
                                BorderWidths.DEFAULT
                        )
                )
        );

        passInput.setBackground(
                new Background(
                        new BackgroundFill(
                                Theme.getConfiguration()
                                        .getTertiaryBackground()
                                        .getStart(),
                                new CornerRadii(3),
                                Insets.EMPTY
                        )
                )
        );

        passInput.setBorder(
                new Border(
                        new BorderStroke(
                                Theme.getConfiguration()
                                        .getTertiaryBorder()
                                        .getStart(),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(2),
                                BorderWidths.DEFAULT
                        )
                )
        );

        HoverEffect effect = Utilities.makeHoverBackgroundLinearGradientEffect(
                Duration.millis(125),
                signInButton,
                Theme.getConfiguration().getPrimaryBackground(),
                Theme.getConfiguration().getPrimaryBorder(),
                Theme.getConfiguration().getPrimaryText()
        );

        Utilities.initializeTextFields(
                Duration.millis(125),
                Theme.getConfiguration().getTertiaryBorder(),
                Theme.getConfiguration().getTertiaryBackground(),
                unameInput, passInput
        );

        signInButton.setOnMouseEntered(mouseEvent -> effect.playOnHover());

        signInButton.setOnMouseExited(mouseEvent -> effect.playOnExit());

        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                signInButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot sign in:\n", validator.createStringBinding()));
        signInButton.setOnAction(this::signInOnAction);


        // Handles the event where the enter key is pressed while the
        // unameInput TextField is selected
        unameInput.setOnKeyPressed(this::signInFromEnterKey);


        // Handles the event where the enter key is pressed while the
        // passInput TextField is selected
        passInput.setOnKeyPressed(this::signInFromEnterKey);
        background.getChildren().add(createAccountWrapper);

        validator.createCheck()
                .withMethod(c -> {
                    if (!Database.authenticateUser(unameInput.getText(), passInput.getText())) {
                        c.error("Wrong username or password, please try again.");
                    }
                })
                .dependsOn("uNameInput", unameInput.textProperty())
                .dependsOn("passInput", passInput.textProperty())
                .decorates(unameInput)
                .decorates(passInput)
                .immediate();


        titleFontSize.bind(background.widthProperty()
                .add(background.heightProperty())
                .divide(1280 + 720)
                .multiply(100)
                .multiply(20.0 / 27.5)
        );

        background.widthProperty().addListener(((observableValue, number, t1) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            labelFontSize.set(Font.font(result * 0.75));
            fieldWidthSize.set(result * 7);
            fieldHeightSize.set(result * 1.25);
            buttonPaddingSize.set(new Insets(result * (0.25), result * (26.0 / 20.0), result * (0.25), result * (26.0 / 20.0)));
            usernameRowSpacing.set(result * 0.95);
            passwordRowSpacing.set(result * 1.15);
            VBox.setMargin(
                    Title,
                    new Insets(
                            0,
                            0,
                            result * 0.225,
                            0
                    )
            );
            VBox.setMargin(
                    usernameRow,
                    new Insets(
                            result * 0.2,
                            0,
                            0,
                            0
                    )
            );
            VBox.setMargin(
                    passwordRow,
                    new Insets(
                            0,
                            0,
                            result * 0.3875,
                            0
                    )
            );
            VBox.setMargin(
                    createAccountWrapper,
                    new Insets(
                            result * 0.3875,
                            0,
                            0,
                            0
                    )
            );
            spacing.set(result * 0.6);
        }));

        background.heightProperty().addListener(((observableValue, number, t1) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            labelFontSize.set(Font.font(result * 0.75));
            fieldWidthSize.set(result * 7);
            fieldHeightSize.set(result * 1.25);
            buttonPaddingSize.set(new Insets(result * (0.25), result * (26.0 / 20.0), result * (0.25), result * (26.0 / 20.0)));
            usernameRowSpacing.set(result * 0.95);
            passwordRowSpacing.set(result * 1.15);
            VBox.setMargin(
                    Title,
                    new Insets(
                            0,
                            0,
                            result * 0.225,
                            0
                    )
            );
            VBox.setMargin(
                    usernameRow,
                    new Insets(
                            result * 0.2,
                            0,
                            0,
                            0
                    )
            );
            VBox.setMargin(
                    passwordRow,
                    new Insets(
                            0,
                            0,
                            result * 0.3875,
                            0
                    )
            );
            VBox.setMargin(
                    createAccountWrapper,
                    new Insets(
                            result * 0.3875,
                            0,
                            0,
                            0
                    )
            );
            spacing.set(result * 0.6);
        }));

        background.spacingProperty().bind(spacing);

        Title.fontProperty().bind(titleFontTracking);

        Username.fontProperty().bind(labelFontSize);
        Password.fontProperty().bind(labelFontSize);
        signInButton.fontProperty().bind(labelFontSize);

        unameInput.prefWidthProperty().bind(fieldWidthSize);
        unameInput.prefHeightProperty().bind(fieldHeightSize);
        passInput.prefWidthProperty().bind(fieldWidthSize);
        passInput.prefHeightProperty().bind(fieldHeightSize);

        unameInput.fontProperty().bind(labelFontSize);
        passInput.fontProperty().bind(labelFontSize);

        usernameRow.spacingProperty().bind(
                usernameRowSpacing
        );
        passwordRow.spacingProperty().bind(
                passwordRowSpacing
        );
    }

    /**
     * Sign the user into their account. This function can be triggered through
     * either pressing the sign-in button or pressing the enter key in one of the
     * TextFields
     */
    private void signInOnAction(ActionEvent actionEvent) {
        transferData();
        View.closeWindow(actionEvent);
        loadTheme();
        View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
        clearFields();
    }

    /**
     * Sign in the user depending on if
     * they hit the enter key instead of the
     * sign button.
     *
     * @param e The key the user clicked.
     */
    private void signInFromEnterKey(KeyEvent e) {
        if (KeyCode.ENTER == e.getCode() && !signInButton.isDisabled()) {
            transferData();
            View.closeWindow(e);
            loadTheme();
            View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
            clearFields();
        }
    }

    /**
     * Clear the TextFields for the username and password
     * once the user has signed in.
     */
    private void clearFields() {
        unameInput.clear();
        passInput.clear();
    }

    /**
     * To load in all the accounts inside the Account View
     * for the user that just signed in.
     */
    private void transferData() {
        User user = Database.getUser();
        if (user != null) {
            ((AccountView) AccountView.getInstance()).getAccountViewController().addAccounts(user.getAccounts());
        }
    }

    /**
     * Load theme for the user that just signed
     * in.
     */
    private void loadTheme() {
        User user = Database.getUser();
        if (user != null && !user.getCurrentTheme().equals("Light")) {
            if (user.getCurrentTheme().equals("High Contrast")) {
                Command command = new SwitchToHighContrastMode(views);
                ThemeSwitcher switcher = new ThemeSwitcher(command);
                switcher.switchTheme();
            } else if (user.getCurrentTheme().equals("Dark")) {
                Command command = new SwitchToDarkMode(views);
                ThemeSwitcher switcher = new ThemeSwitcher(command);
                switcher.switchTheme();
            }
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
}