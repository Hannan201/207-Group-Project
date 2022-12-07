package controllers;

import data.Database;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.synedra.validatorfx.TooltipWrapper;
import views.AccountView;
import views.HomePageView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import views.AccountView;
import views.HomePageView;

/**
 * A conroller for the Sign-in UI
 */
public class SignInController implements Initializable {

    private Validator validator = new Validator();
    @FXML
    HBox box;
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
        /**
         * Handles the event where the enter key is pressed while the
         * unameInput Textfield is selected
         */
    unameInput.setOnKeyPressed(e -> {
        if (KeyCode.ENTER == e.getCode() && !signInButton.isDisabled()) {
            // Switch Scene Accordingly
        }
    });
        /**
         * Handles the event where the enter key is pressed while the
         * passInput Textfield is selected
         */
    passInput.setOnKeyPressed(e -> {
        if (KeyCode.ENTER == e.getCode() && !signInButton.isDisabled()) {
            // Switch Scene Accordingly
        }
    });
    box.getChildren().add(createAccountWrapper);

//        validator.createCheck()
//                .withMethod(c -> {
//                    if (Database.authenticateUser(unameInput.getText(), passInput.getText())){
//                        c.error("Wrong username or password, please try again.");
//                    }
//                })
//                .dependsOn("uNameInput", unameInput.textProperty())
//                .dependsOn("passInput", passInput.textProperty())
//                .decorates(unameInput)
//                .decorates(passInput)
//                .immediate();

    }

    /**
     * Sign the user into their account. This function can be triggered through
     * either pressing the sign-in button or pressing the enter key in one of the
     * textfields
     */
    private void signInOnAction(ActionEvent actionEvent) {
    }

}






