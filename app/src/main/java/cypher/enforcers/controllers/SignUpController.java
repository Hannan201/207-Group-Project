package cypher.enforcers.controllers;

import cypher.enforcers.models.UserModel;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.themes.Theme;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.views.AccountView;
import cypher.enforcers.views.HomePageView;
import cypher.enforcers.views.View;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the Sign-up UI.
 */
public class SignUpController implements Initializable {

    // Logger for the sign-up controller.
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    // Used for validation based on what the user types.
    private final Validator validator = new Validator();

    // Contains all the content to be displayed vertically
    // in the center.
    @FXML
    public VBox box;

    // Button that says sign-up.
    @FXML
    public Button signUp;

    // Text field for the initial username.
    @FXML
    public TextField initialUsername;

    // Text field for the verified username.
    @FXML
    public TextField verifiedUsername;

    // Text field for the initial password.
    @FXML
    public PasswordField initialPassword;

    // Text field for the verified password.
    @FXML
    public PasswordField verifiedPassword;

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Clear all the text fields when this window closes. Since
        // we're using singleton pattern, only one view instance will be
        // created.
        box.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        newWindow.setOnHidden((windowEvent -> {
                            logger.trace("Clearing all text fields.");
                            initialUsername.clear();
                            initialPassword.clear();
                            verifiedUsername.clear();
                            verifiedPassword.clear();
                        }));
                    }
                }));
            }
        }));

        signUp = new Button("Sign Up");
        signUp.setPrefSize(130, 25);

        // creates the decorated button
        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                signUp,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot sign up:\n", validator.createStringBinding()));
        box.getChildren().add(createAccountWrapper);

        signUp.setOnAction(this::handleSignUp);

        // checks if the username field is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (initialUsername.getText().isEmpty()) {
                        c.error("Please add your username!");
                    }
                })
                .dependsOn("initialUsername", initialUsername.textProperty())
                .decorates(initialUsername)
                .immediate();

        // tells the user to add their verified username

        validator.createCheck()
                .withMethod(c -> {
                    if (verifiedUsername.getText().isEmpty() && !initialUsername.getText().isEmpty()) {
                        c.error("Please verify your username!");
                    }
                })
                .dependsOn("verifiedUsername", verifiedUsername.textProperty())
                .decorates(verifiedUsername)
                .immediate();

        // checks if both usernames are the same

        validator.createCheck()
                .withMethod(c -> {
                    if (!initialUsername.getText().equals(verifiedUsername.getText())) {
                        c.error("Your usernames do not match, please try again.");
                    }
                })
                .dependsOn("initialUsername", initialUsername.textProperty())
                .dependsOn("verifiedUsername", verifiedUsername.textProperty())
                .decorates(initialUsername)
                .decorates(verifiedUsername)
                .immediate();

        // checks if the password was inputted

        validator.createCheck()
                .withMethod(c -> {
                    if (initialPassword.getText().isEmpty()) {
                        c.error("Please add your password!");
                    }
                })
                .dependsOn("initialPassword", initialPassword.textProperty())
                .decorates(initialPassword)
                .immediate();

        // checks if the verified password was inputted
        // after the user puts their normal password

        validator.createCheck()
                .withMethod(c -> {
                    if (verifiedPassword.getText().isEmpty() && !initialPassword.getText().isEmpty()) {
                        c.error("Please verify your password!");
                    }
                })
                .dependsOn("verifiedPassword", verifiedPassword.textProperty())
                .decorates(verifiedPassword)
                .immediate();

        // checks if both passwords inputted match each other

        validator.createCheck()
                .withMethod(c -> {
                    if (!initialPassword.getText().equals(verifiedPassword.getText())) {
                        c.error("Your passwords do not match, please try again.");
                    }
                })
                .dependsOn("initialPassword", initialPassword.textProperty())
                .dependsOn("verifiedPassword", verifiedPassword.textProperty())
                .decorates(initialPassword)
                .decorates(verifiedPassword)
                .immediate();

        // checks if the Account is already registered

        validator.createCheck()
                .withMethod(c -> {
                    // Only show this error on two conditions:
                    // 1 ) Initial username is not empty.
                    // 2 ) Initial username and verified username are not
                    // empty and equal each other. This prevents the
                    // database from having to do a useless search and
                    // prevents ValidatorFX from overriding the previous
                    // alert with this alert.

                    if (initialUsername.getText().isEmpty() || !initialUsername.getText().equals(verifiedUsername.getText())) {
                        return;
                    }

                    if (userModel.isUsernameTaken(initialUsername.getText())) {
                        c.error("This account is already registered.");
                    }
                })
                .dependsOn("initialUsername", initialUsername.textProperty())
                .dependsOn("verifiedUsername", verifiedUsername.textProperty())
                .decorates(initialUsername)
                .decorates(verifiedUsername)
                .immediate();
    }

    /**
     * A handle method tied to the Sign-Up button in the initialize method
     * which is responsible for transitioning from the Sign-Up view
     * to the Accounts view.
     *
     * @param e ActionEven that triggered this
     *          handle method.
     */
    public void handleSignUp(ActionEvent e) {
        // Switching theme (sample code), should be included in the Controller class event handlers
        // This does not work for popups because those need a new stage to be created

        // open pop up

        if (userModel.registerUser(initialUsername.getText(), initialPassword.getText())) {
            View.closeWindow((Node) e.getSource());

            // Since by default, this application is light mode.
            Utilities.adjustTheme(Theme.LIGHT);

            logger.trace("Switching from the HomePageView to the AccountsView.");
            View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());

            // Clear the attributes such that when the signs out
            // they do not have access to the credentials <-- Now done in the initialize method.
        }
    }
}
