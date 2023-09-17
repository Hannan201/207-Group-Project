package cypher.enforcers.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.data.database.Database;
import cypher.enforcers.data.Storage;
import cypher.enforcers.data.security.Token;
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
import cypher.enforcers.views.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the Sign-in UI
 */
public class SignInController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

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
        Utilities.loadAccounts();
        View.closeWindow(actionEvent);
        Utilities.adjustTheme();

        logger.trace("Switching from the HomePageView to the AccountsView.");
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
            Utilities.loadAccounts();
            View.closeWindow(e);
            Utilities.adjustTheme();

            logger.trace("Switching from the HomePageView to the AccountsView.");
            View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());

            clearFields();
        }
    }

    /**
     * Clear the TextFields for the username and password
     * once the user has signed in.
     */
    private void clearFields() {
        logger.trace("Clearing fields.");
        unameInput.clear();
        passInput.clear();
    }
}