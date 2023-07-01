package controllers;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.managers.ThemeSwitcher;
import data.database.Database;
import data.Storage;
import data.security.Token;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
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

    @FXML
    private HBox box;

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

    /**
     * TextField for the user to type their username
     */
    @FXML
    private TextField unameInput;

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

    @FXML
    private Region aboveTitle;

    @FXML
    private Region belowButton;

    @FXML
    private final DoubleProperty delta = new SimpleDoubleProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aboveTitle.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
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

        aboveTitle.prefHeightProperty().bind(
                background.heightProperty()
                        .multiply(27.0 / 250.0)
        );

        belowButton.prefHeightProperty().bind(
                aboveTitle.prefHeightProperty()
                        .multiply(37.0 / 27.0)
        );

        boolean[] isBelow = {false};

        signInButton = new Button("Sign In");
        signInButton.setPrefHeight(25);
        signInButton.setPrefWidth(100);
        signInButton.setAlignment(Pos.CENTER);

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
        box.getChildren().add(createAccountWrapper);

        validator.createCheck()
                .withMethod(c -> {
                    Token token = Database.authenticateUser(unameInput.getText(), passInput.getText());
                    if (token == null) {
                        c.error("Wrong username or password, please try again.");
                    } else {
                        Storage.setToken(token);
                    }
                })
                .dependsOn("uNameInput", unameInput.textProperty())
                .dependsOn("passInput", passInput.textProperty())
                .decorates(unameInput)
                .decorates(passInput)
                .immediate();

        background.widthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() < Title.getWidth()) {
                delta.set(Title.getWidth() - t1.doubleValue());
                if (!isBelow[0]) {
                    Title.setMaxHeight(Title.getPrefHeight() * 2);
                    aboveTitle.prefHeightProperty().bind(aboveTitle.heightProperty().subtract(delta));
                    belowButton.prefHeightProperty().bind(belowButton.heightProperty().subtract(delta));
                    isBelow[0] = true;
                }
            } else {
                if (isBelow[0]) {
                    Title.setMaxHeight(Title.getPrefHeight());
                    aboveTitle.prefHeightProperty().bind(
                            background.heightProperty()
                                    .multiply(27.0 / 250.0)
                    );
                    belowButton.prefHeightProperty().bind(
                            aboveTitle.prefHeightProperty()
                                    .multiply(37.0 / 27.0)
                    );
                    isBelow[0] = false;
                }
            }
        });
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
        ((AccountView) AccountView.getInstance()).getAccountViewController()
                .addAccounts(Database.getAccounts(Storage.getToken()));
    }

    /**
     * Load theme for the user that just signed
     * in.
     */
    private void loadTheme() {
        String theme = Database.getTheme(Storage.getToken());
        if (theme != null && !theme.equals("light mode")) {
            if (theme.equals("high contrast mode")) {
                Command command = new SwitchToHighContrastMode(views);
                ThemeSwitcher switcher = new ThemeSwitcher(command);
                switcher.switchTheme();
            } else if (theme.equals("dark mode")) {
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