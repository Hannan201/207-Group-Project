package cypher.enforcers.controllers;

import cypher.enforcers.models.AccountModel;
import cypher.enforcers.models.UserModel;
import cypher.enforcers.views.View;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the create-account view.
 */
public class CreateAccountController implements Initializable {

    /** Logger for the create account view. */
    private static final Logger logger = LoggerFactory.getLogger(CreateAccountController.class);

    /** Used for validation based on what the user types. */
    private final Validator validator = new Validator();

    /** Contains all content to be displayed for this application. */
    @FXML
    public VBox box;

    /** Button that says create account. */
    public Button createAccount;

    /** Button for the GitHub icon. */
    @FXML
    public ToggleButton github;

    /** Button for the Google icon. */
    @FXML
    public ToggleButton google;

    /** Button for the Shopify icon. */
    @FXML
    public ToggleButton shopify;

    /** Button for the Discord icon. */
    @FXML
    public ToggleButton discord;

    /** Text field for the Social Media type. */
    @FXML
    public TextField platform;

    /** Text field for the name of the account. */
    @FXML
    public TextField username;

    /** To interact with the current user. */
    private final UserModel userModel;

    /** To interact with the user's accounts. */
    private final AccountModel accountModel;

    /**
     * Create the controller for the create-account view with the
     * required models.
     *
     * @param userModel The model to interact with the users.
     * @param accountModel The mode to interact with the accounts.
     */
    public CreateAccountController(UserModel userModel, AccountModel accountModel) {
        this.userModel = userModel;
        this.accountModel = accountModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Clear all the text fields when this window closes. Since
        // we're using the singleton pattern, only one view instance will be
        // created.
        View.onPopUpWindowClose(box, windowEvent -> {
            logger.trace("Clearing all text fields and allowing for text field to be edited.");
            platform.clear();
            username.clear();
            platform.setEditable(true);
        });

        createAccount = new Button("Create Account");
        createAccount.setPrefSize(155, 32);
        createAccount.setContentDisplay(ContentDisplay.CENTER);

        // creates the decorated button
        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                createAccount,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot add account:\n", validator.createStringBinding())
        );

        // adds the Account to the ListView in AccountsView when the button is clicked
        createAccount.setOnAction(c -> {
            if (accountModel.addAccount(userModel.getCurrentUser().id(), username.getText(), platform.getText())) {
                View.closeWindow((Node) c.getSource());
            }
        });

        box.getChildren().add(createAccountWrapper); // adds the decorated button to the HBox

        // Renders a button un-clickable and adds a hover message over the button
        // if there exists an Account with the same username and platform
        validator.createCheck()
                .withMethod(c -> {
                    if (!(username.getText().isEmpty() || platform.getText().isEmpty()) && accountModel.checkDuplicate(username.getText(), platform.getText())) {
                        c.error("This account already exists, please try again!");
                    }
                })
                .dependsOn("username", username.textProperty())
                .dependsOn("platform", platform.textProperty())
                .decorates(username)
                .decorates(platform)
                .immediate();

        // Renders a button un-clickable and adds a hover message over the button
        // if the text in the username TextField is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (username.getText().isEmpty()) {
                        c.error("Empty username/email.");
                    }
                })
                .dependsOn("username", username.textProperty())
                .decorates(username)
                .immediate();

        // Renders a button un-clickable and adds a hover message over the button
        // if the text in the platform TextField is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (platform.getText().isEmpty()) {
                        c.error("Empty platform.");
                    }
                })
                .dependsOn("platform", platform.textProperty())
                .decorates(platform)
                .immediate();
    }

    /**
     * Toggle the platform so that:
     * - if it's editable: make it uneditable and then attach text to it.
     * - If it's uneditable: make it editable and then clear the text.
     *
     * @param text Message to display if the text field is
     *             uneditable.
     */
    public void toggle(String text) {
        boolean result = platform.isEditable();
        platform.setText(result ? text : "");
        platform.setEditable(!result);
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the GitHub toggle button is pressed.
     */
    public void handleGithub() {
        toggle("Github");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Google toggle button is pressed.
     */
    public void handleGoogle() {
        toggle("Google");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the shopify toggle button is pressed.
     */
    public void handleShopify() {
        toggle("Shopify");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Discord toggle button is pressed.
     */
    public void handleDiscord() {
        toggle("Discord");
    }
}
