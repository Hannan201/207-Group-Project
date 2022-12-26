package controllers;

import data.Database;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import views.AccountView;
import views.HomePageView;
import views.View;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    private final Validator validator = new Validator();
    @FXML
    public Button signUp;

    @FXML
    public VBox box;

    @FXML
    public PasswordField initialPassword;

    @FXML
    public PasswordField verifiedPassword;

    @FXML
    public TextField initialUsername;

    @FXML
    public TextField verifiedUsername;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        signUp = new Button("Sign Up");
        signUp.setPrefHeight(25);
        signUp.setPrefWidth(130);
        // creates the decorated button
        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                signUp,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot sign up:\n", validator.createStringBinding()));
        signUp.setOnAction(this::handleSignUp);
        box.getChildren().add(createAccountWrapper);

        // checks if the username field is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (initialUsername.getText().equals("")){
                        c.error("Please add your username!");
                    }
                })
                .dependsOn("initialUsername", initialUsername.textProperty())
                .decorates(initialUsername)
                .immediate()
        ;

        // tells the user to add their verified username

        validator.createCheck()
                .withMethod(c -> {
                    if (verifiedUsername.getText().equals("") && !initialUsername.getText().equals("")){
                        c.error("Please verify your username!");
                    }
                })
                .dependsOn("verifiedUsername", verifiedUsername.textProperty())
                .decorates(verifiedUsername)
                .immediate()
        ;

        // checks if both usernames are the same

        validator.createCheck()
                .withMethod(c -> {
                    if (! initialUsername.getText().equals(verifiedUsername.getText())){
                        c.error("Your usernames do not match, please try again.");
                    }
                })
                .dependsOn("initialUsername", initialUsername.textProperty())
                .dependsOn("verifiedUsername", verifiedUsername.textProperty())
                .decorates(initialUsername)
                .decorates(verifiedUsername)
                .immediate()
        ;

        // checks if the password was inputted

        validator.createCheck()
                .withMethod(c -> {
                    if (initialPassword.getText().equals("")){
                        c.error("Please add your password!");
                    }
                })
                .dependsOn("initialPassword", initialPassword.textProperty())
                .decorates(initialPassword)
                .immediate()
        ;

        // checks if the verified password was inputted
        // after the user puts their normal password

        validator.createCheck()
                .withMethod(c -> {
                    if (verifiedPassword.getText().equals("") && !initialPassword.getText().equals("")){
                        c.error("Please verify your password!");
                    }
                })
                .dependsOn("verifiedPassword", verifiedPassword.textProperty())
                .decorates(verifiedPassword)
                .immediate();

        // checks if both passwords inputted match each other

        validator.createCheck()
                .withMethod(c -> {
                    if (! initialPassword.getText().equals(verifiedPassword.getText())){
                        c.error("Your passwords do not match, please try again.");
                    }
                })
                .dependsOn("initialPassword", initialPassword.textProperty())
                .dependsOn("verifiedPassword", verifiedPassword.textProperty())
                .decorates(initialPassword)
                .decorates(verifiedPassword)
                .immediate();

        // checks if the Account is already registered

        validator.createCheck()
                .withMethod(c -> {
                    if (Database.checkUsername(initialUsername.getText())){
                        c.error("This account is already registered.");
                    }
                })
                .dependsOn("initialUsername", initialUsername.textProperty())
                .dependsOn("verifiedUsername", verifiedUsername.textProperty())
                .decorates(initialUsername)
                .decorates(verifiedUsername)
                .immediate();
    }

    /**
     * A handle method tied to the Sign-Up button in the initialize method
     * which is responsible for transitioning from the Sign-Up view
     * to the Accounts view.
     *
     * @param e ActionEven that triggered this
     *          handle method.
     */
    public void handleSignUp(ActionEvent e) {
        // Switching theme (sample code), should be included in the Controller class event handlers
        // This does not work for popups because those need a new stage to be created

        // open pop up

        View.closeWindow(e);

        View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());

        // Clear the attributes such that when the signs out
        // they do not have access to the credentials

        Database.registerUser(initialUsername.getText(), initialPassword.getText());

        initialUsername.clear();
        initialPassword.clear();
        verifiedUsername.clear();
        verifiedPassword.clear();
    }
}
