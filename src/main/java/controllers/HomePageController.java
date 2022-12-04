package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import views.SignUpView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomePageController {

    public Button signIn;
    public Button signUp;
    public void handleSignIn(ActionEvent e) {

        // open pop up
        System.out.println("Signed in.");
    }

    public void handleSignUp(ActionEvent e) {

        Stage stage = new Stage();
        // no need to clear, because the stylesheets will always be empty
        if (SignUpView.getInstance().getRoot().getScene() == null) {
            Scene scene = new Scene(SignUpView.getInstance().getRoot());
            scene.getStylesheets().add(SignUpView.getInstance().getCurrentThemePath());
            stage.setScene(scene);
        }
        else {
            stage.setScene(SignUpView.getInstance().getRoot().getScene());
        }
        stage.show();
        System.out.println("Signed up.");}

}
