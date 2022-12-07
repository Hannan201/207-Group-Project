package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import views.SignInView;
import views.SignUpView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import views.View;

public class HomePageController {

    public Button signIn;
    public Button signUp;

    /**
     *
     * A handle method for the signIn button that opens a pop up to allow the
     * user to sign in.
     *
     * @param e
     */
    public void handleSignIn(ActionEvent e) {
        View.loadNewWindow(SignInView.getInstance());
    }

    /**
     *
     * A handle method for the signUp button that opens a pop-up to allow the
     * user to sign in.
     *
     * @param e
     */
    public void handleSignUp(ActionEvent e) {
        View.loadNewWindow(SignUpView.getInstance());
    }
}
