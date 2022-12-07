package controllers;

import data.Database;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import user.User;
import views.AccountView;
import views.HomePageView;
import views.View;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A conroller for the Sign-in UI
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
     * Username label
     */
    @FXML
    private Label Username;

    /**
     * Textfield for the user to type their username
     */
    @FXML
    private TextField unameInput;

    /**
     * Password label
     */
    @FXML
    private Label Password;

    /**
     * Textfield for the user to type their password
     */
    @FXML
    private TextField passInput;

    /**
     * Sign in button that triggers the sign in event
     */
    @FXML
    private Button signInButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        unameInput.setOnKeyPressed(e -> {
            if (KeyCode.ENTER == e.getCode() && !signInButton.isDisabled()) {
                transferData();
                View.closeWindow(e);
                View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
                clearFields();
            }
        });


    // Handles the event where the enter key is pressed while the
    // passInput TextField is selected
    passInput.setOnKeyPressed(e -> {
        if (KeyCode.ENTER == e.getCode() && !signInButton.isDisabled()) {
            transferData();
            View.closeWindow(e);
            View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
            clearFields();
        }
    });
    box.getChildren().add(createAccountWrapper);

    validator.createCheck()
            .withMethod(c -> {
                if (!Database.authenticateUser(unameInput.getText(), passInput.getText())){
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
     */
    private void signInOnAction(ActionEvent actionEvent) {
        transferData();
        View.closeWindow(actionEvent);
        View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
        clearFields();
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
}