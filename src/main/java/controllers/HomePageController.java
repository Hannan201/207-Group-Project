package controllers;

import javafx.event.ActionEvent;
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

        System.out.println("Signed up.");}

}
