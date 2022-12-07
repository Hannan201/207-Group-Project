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

        // Once the sign-in view is complete
        // you can just do this
//        loadNewWindow(SignInView.getInstance());

        loadNewWindow(SignInView.getInstance());
        System.out.println("Signed up.");
    }

    /**
     *
     * A handle method for the signUp button that opens a pop up to allow the
     * user to sign in.
     *
     * @param e
     */
    public void handleSignUp(ActionEvent e) {

//        Stage stage = new Stage();
//        // no need to clear, because the stylesheets will always be empty
//        if (SignUpView.getInstance().getRoot().getScene() == null) {
//            Scene scene = new Scene(SignUpView.getInstance().getRoot());
//            scene.getStylesheets().add(SignUpView.getInstance().getCurrentThemePath());
//            stage.setScene(scene);
//        }
//        else {
//            stage.setScene(SignUpView.getInstance().getRoot().getScene());
//        }
//        stage.show();

        loadNewWindow(SignUpView.getInstance());
        System.out.println("Signed up.");
    }

    /**
     * Loads a new pop up window for the user to interact with.
     * @param view
     */
    private void loadNewWindow(View view) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        if (view.getRoot().getScene() == null) {
            Scene scene = new Scene(view.getRoot());
            scene.getStylesheets().add(view.getCurrentThemePath());
            stage.setScene(scene);
        } else {
            stage.setScene(view.getRoot().getScene());
        }
        stage.show();
    }

}
