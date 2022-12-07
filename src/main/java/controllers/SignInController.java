package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.AccountView;
import views.HomePageView;

/**
 * A conroller for the Sign-in UI
 */
public class SignInController {
    /**
     * Background of the scene
     */
    @FXML
    VBox background;
    /**
     * Title of the scene
     */
    @FXML
    Label Title;

    /**
     * Username label
     */
    @FXML
    Label Username;

    /**
     * Textfield for the user to type their username
     */
    @FXML
    TextField unameInput;

    /**
     * Password label
     */
    @FXML
    Label Password;

    /**
     * Textfield for the user to type their password
     */
    @FXML
    TextField passInput;

    /**
     * Sign in button that triggers the sign in event
     */
    @FXML
    Button signInButton;

    /**
     * Sign the user into their account. This function can be triggered through
     * either pressing the sign-in button or pressing the enter key in one of the
     * textfields
     */
    public void signIn() {
        signInButton.setOnAction(event -> {
            //TODO this should trigger the sign in functionality
        });
    }


    /**
     * Handles the event where the sign-in button is clicked
     */
    public void signInOnAction() {
        signInButton.setOnAction(event -> {
            signIn();
        });
    }

    /**
     * Handles the event where the enter key is pressed while the
     * unameInput Textfield is selected
     */
    public void unameInputOnEnter() {
        unameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signIn();
            }
        });
    }

    /**
     * Handles the event where the enter key is pressed while the
     * passInput Textfield is selected
     */
    public void passInputOnEnter() {
        passInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signIn();
            }
        });
    }

}






