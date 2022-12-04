package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
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

        // Close current window.
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        // Change homepage view to account view.
        Scene scene = HomePageView.getInstance().getRoot().getScene();
        scene.getStylesheets().clear();

        scene.setRoot(AccountView.getInstance().getRoot());
        scene.getStylesheets().add(AccountView.getInstance().getCurrentThemePath());
    }
}
