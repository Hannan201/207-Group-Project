package cypher.enforcers.controllers;

import cypher.enforcers.models.UserModel;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.accountview.AccountView;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the Sign-in UI
 */
public class SignInController implements Initializable {

    // Logger for the sign-in controller.
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    // Used for validation based on what the user types.
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
    private Label title;

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

    // Container to hold the sign-in button.
    @FXML
    private HBox box;

    /**
     * Sign in button that triggers the sign in event
     */
    @FXML
    private Button signInButton;

    // Allows the spacing above the title to be controlled.
    @FXML
    private Region aboveTitle;

    // Allows the spacing below the title to be controlled.
    @FXML
    private Region belowButton;

    // If the window's width is less than the width of the title's,
    // then this property is used to store the difference between the
    // two values.
    @FXML
    private final DoubleProperty delta = new SimpleDoubleProperty();

    // To interact with the user data.
    private UserModel userModel;

    /**
     * Set the user model.
     *
     * @param model The User Model.
     */
    public void setUserModel(UserModel model) {
        this.userModel = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Clear all the text fields when this window closes. Since
        // we're using singleton pattern, only one view instance will be
        // created.
        aboveTitle.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        newWindow.setOnHidden(windowEvent -> {
                            logger.trace("Clearing fields.");
                            unameInput.clear();
                            passInput.clear();
                        });
                    }
                }));
            }
        }));

        // The spacings above and below the title were not equal when
        // this project was first submitted. Since I wasn't
        // responsible for the UI and didn't want to tamper with to
        // too much, I made it so the original spacings were maintained.
        // You can change them if you wish - by Hannan201.

        aboveTitle.prefHeightProperty().bind(
                background.heightProperty()
                        .multiply(27.0 / 250.0) // Random value found from trial and
                                                     // error to maintain original spacing
                                                     // for when this project was first
                                                     // submitted.
        );

        belowButton.prefHeightProperty().bind(
                aboveTitle.prefHeightProperty()
                        .multiply(37.0 / 27.0) // Random value found from trial and
                                                    // error to maintain original spacing
                                                    // for when this project was first
                                                    // submitted.
        );

        // This is used to track if the window's current width is
        // less than the title's width. Java doesn't allow
        // using boolean variables inside a lambda function, so I had
        // to make it an array.
        boolean[] isBelow = {false};

        background.widthProperty().addListener((observableValue, number, t1) -> {
            /*
               When the window's width is less than the width of the
               title's, then the spacing above and below the title wil
               shrink, and the max height of the tittle will increase.
               This gives the title more room to expand its height and
               thus be able to wrap the text.

               The boolean is used to control if the window's width is
               smaller or not, this avoids repeating the bindings to adjust
               the spacing above and below.
             */

            if (t1.doubleValue() < title.getWidth()) {
                delta.set(title.getWidth() - t1.doubleValue());
                if (!isBelow[0]) {
                    title.setMaxHeight(title.getPrefHeight() * 2);
                    aboveTitle.prefHeightProperty().bind(
                            aboveTitle.heightProperty().subtract(delta)
                    );
                    belowButton.prefHeightProperty().bind(
                            belowButton.heightProperty().subtract(delta)
                    );
                    isBelow[0] = true;
                }
            } else {
                if (isBelow[0]) {
                    title.setMaxHeight(title.getPrefHeight());
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

        signInButton = new Button("Sign In");
        signInButton.setPrefSize(100, 25);
        signInButton.setAlignment(Pos.CENTER);
        signInButton.setOnAction(event -> {
            try {
                signInOnAction(event);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                signInButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot sign in:\n", validator.createStringBinding()));
        box.getChildren().add(createAccountWrapper);

        // Handles the event where the enter key is pressed while the
        // unameInput TextField is selected
        unameInput.setOnKeyPressed(event -> {
            try {
                signInFromEnterKey(event);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        // Handles the event where the enter key is pressed while the
        // passInput TextField is selected
        passInput.setOnKeyPressed(event -> {
            try {
                signInFromEnterKey(event);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        // No use to send a query to the database if the username or password
        // is empty. The database does have checks to ensure the data is
        // not empty, however this would then trigger a log message. Might
        // as well alert the user here, so they can see it.
        validator.createCheck()
                .withMethod(c -> {
                    if (unameInput.getText().isEmpty()) {
                        c.error("Empty username.");
                    }
                })
                .dependsOn("uNameInput", unameInput.textProperty())
                .decorates(unameInput)
                .immediate();

        validator.createCheck()
                .withMethod(c -> {
                    if (passInput.getText().isEmpty()) {
                        c.error("Empty password.");
                    }
                })
                .dependsOn("passInput", passInput.textProperty())
                .decorates(passInput)
                .immediate();

        validator.createCheck()
                .withMethod(c -> {
                    // This alert should only be shown if the
                    // username and password are not empty. Since
                    // Validator FX would override the previous alerts
                    // with this alert if inputs were empty, and empty
                    // inputs would trigger a log message from the database
                    // which the user won't see unless they check the console
                    // or file. Easier to alter the user here.
                    if (unameInput.getText().isEmpty() ||
                        passInput.getText().isEmpty()) {
                        return;
                    }

                    if (!userModel.logInUser(unameInput.getText(), passInput.getText())) {
                        c.error("Wrong username or password, please try again.");
                    }
                })
                .dependsOn("uNameInput", unameInput.textProperty())
                .dependsOn("passInput", passInput.textProperty())
                .decorates(unameInput)
                .decorates(passInput)
                .immediate();
    }

    /**
     * Sign the user into their account. This function can be triggered through
     * either pressing the sign-in button or pressing the enter key in one of the
     * TextFields
     *
     * @throws IOException if any errors occur while loading in the
     * accounts view.
     * @throws NullPointerException If the accounts view cannot be created
     * due to missing data or if the theme for the user cannot be set due
     * to missing data.
     */
    private void signInOnAction(ActionEvent actionEvent) throws IOException, NullPointerException {
        View.closeWindow((Node) actionEvent.getSource());

        Utilities.adjustTheme(userModel.getCurrentUser().theme());

        logger.trace("Switching from the HomePageView to the AccountsView.");
        View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
    }

    /**
     * Sign in the user depending on if
     * they hit the enter key instead of the
     * sign button.
     *
     * @param e The key the user clicked.
     * @throws IOException if any errors occur while loading in the
     * accounts view.
     * @throws NullPointerException If the accounts view cannot be created
     * due to missing data or if the theme for the user cannot be set due
     * to missing data.
     */
    private void signInFromEnterKey(KeyEvent e) throws IOException, NullPointerException {
        if (KeyCode.ENTER == e.getCode() && !signInButton.isDisabled()) {
            View.closeWindow((Node) e.getSource());

            Utilities.adjustTheme(userModel.getCurrentUser().theme());

            logger.trace("Switching from the HomePageView to the AccountsView.");
            View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
        }
    }
}