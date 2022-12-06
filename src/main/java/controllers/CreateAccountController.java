package controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.ValidationMessage;
import net.synedra.validatorfx.Validator;
import user.Account;
import views.AccountView;
import views.AddAccountView;

import java.net.URL;
import java.util.ResourceBundle;

import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.Decoration;
import net.synedra.validatorfx.DefaultDecoration;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.ValidationMessage;
import net.synedra.validatorfx.Validator;

public class CreateAccountController implements Initializable{

    private Validator validator = new Validator();

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
        });

        box.getChildren().add(createAccountWrapper); // adds the decorated button to the HBox

        // Renders a button unclickable and adds a hover message over the button
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

        // Renders a button unclickable and adds a hover message over the button
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

        // Renders a button unclickable and adds a hover message over the button
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

    public void handleGithub(ActionEvent e) {
        if (platform.isEditable()) {
            platform.setText("Github");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    public void handleGoogle(ActionEvent e) {
        if (platform.isEditable()) {
            platform.setText("Google");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    public void handleShopify(ActionEvent e) {
        if (platform.isEditable()) {
            platform.setText("Shopify");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    public void handleDiscord(ActionEvent e) {
        if (platform.isEditable()) {
            platform.setText("Discord");
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }

    }
//    public void handleCreateAccount(ActionEvent e) {
//
////        createAccountWrapper
//
//
//
//
//        // need to use tooltip to ensure that everything is the same...
//    }

}
