package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import net.synedra.validatorfx.Check;
import views.AccountView;
import views.HomePageView;

public class SignUpController {

    public Button signUp;
    public PasswordField initialPassword;
    public PasswordField verifiedPassword;
    public TextField initialUsername;
    public TextField verifiedUsername;

    public void handleSignUp(ActionEvent e) {


        // Switching theme (sample code), should be included in the Controller class event handlers
        // This does not work for popups because those need a new stage to be created

        // open pop up

    }
}
