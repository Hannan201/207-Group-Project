package controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import user.Account;
import views.AccountView;
import views.AddAccountView;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable{

    private final Validator validator = new Validator();

    public Button createAccount;
    @FXML
    public ToggleButton github;

    @FXML
    public ToggleButton google;

    @FXML
    public ToggleButton shopify;

    @FXML
    public ToggleButton discord;

    @FXML
    public TextField platform;

    @FXML
    public TextField username;

    @FXML
    public VBox box;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAccount = new Button("Create Account");
        createAccount.setContentDisplay(ContentDisplay.CENTER);
        createAccount.setPrefHeight(32);
        createAccount.setPrefWidth(155);
        // creates the decorated button
        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                createAccount,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot add account:\n", validator.createStringBinding()));
        // adds the Account to the ListView in AccountsView when the button is clicked
        createAccount.setOnAction(c -> {
            Account account = new Account(username.getText(), platform.getText());
            ((AccountView)AccountView.getInstance()).getAccountViewController().addAccount(account);
            Stage stage = (Stage) AddAccountView.getInstance().getRoot().getScene().getWindow();
            stage.close();
            username.clear();
            platform.clear();
        });

        box.getChildren().add(createAccountWrapper); // adds the decorated button to the HBox

        // Renders a button un-clickable and adds a hover message over the button
        // if there exists an Account with the same username and platform

        validator.createCheck()
                .withMethod(c -> {
                    Account account = new Account(username.getText(), platform.getText());
                    boolean duplicate = ((AccountView)AccountView.getInstance()).getAccountViewController().existsDuplicate(account);
                    if (duplicate && !(username.getText().equals("") || platform.getText().equals(""))){
                        c.error("This account already exists, please try again!");
                    }
                })
                .dependsOn("username", username.textProperty())
                .dependsOn("platform", platform.textProperty())
                .decorates(username)
                .decorates(platform)
                .immediate()
        ;

        // Renders a button un-clickable and adds a hover message over the button
        // if the text in the username TextField is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (username.getText().equals("")){
                        c.error("Empty username/email.");
                    }
                })
                .dependsOn("username", username.textProperty())
                .decorates(username)
                .immediate()
        ;

        // Renders a button un-clickable and adds a hover message over the button
        // if the text in the platform TextField is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (platform.getText().equals("")){
                        c.error("Empty platform.");
                    }
                })
                .dependsOn("platform", platform.textProperty())
                .decorates(platform)
                .immediate()
        ;
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the GitHub toggle button is pressed.
     */
    public void handleGithub() {
        if (platform.isEditable()) {
            platform.setText("Github");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Google toggle button is pressed.
     */
    public void handleGoogle() {
        if (platform.isEditable()) {
            platform.setText("Google");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the shopify toggle button is pressed.
     */
    public void handleShopify() {
        if (platform.isEditable()) {
            platform.setText("Shopify");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Discord toggle button is pressed.
     */
    public void handleDiscord() {
        if (platform.isEditable()) {
            platform.setText("Discord");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }

    }
}
