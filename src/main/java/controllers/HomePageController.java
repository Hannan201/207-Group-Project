package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import views.SignUpView;

public class HomePageController {

    public Button signIn;
    public Button signUp;

    public void handleSignIn(ActionEvent e) {

        // open pop up
        System.out.println("Signed in.");
    }

    public void handleSignUp(ActionEvent e) {

        Stage stage = new Stage();
        Scene scene = new Scene(SignUpView.getInstance().getRoot());
        // no need to clear, because the stylesheets will always be empty
        scene.getStylesheets().add(SignUpView.getInstance().getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
        System.out.println("Signed up.");}

}
