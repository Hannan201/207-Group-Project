package controllers;

import data.Database;
import data.Storage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import models.Account;
import views.AccountView;
import views.AddAccountView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable{

    private final Validator validator = new Validator();

    public Button createAccount;

    @FXML
    private HBox icons;

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

    private final DoubleProperty spacing = new SimpleDoubleProperty(10);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        box.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        newWindow.setOnCloseRequest((windowEvent -> {
                            platform.clear();
                            username.clear();
                        }));
                    }
                }));
            }
        }));

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
            int id = Database.addAccount(Storage.getToken(), username.getText(), platform.getText());
            ((AccountView)AccountView.getInstance()).getAccountViewController().addAccount(id);
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
                    List<Account> accounts = Database.getAccounts(Storage.getToken());
                    Account account = new Account(-1, username.getText(), platform.getText());
                    boolean duplicate = accounts.contains(account);
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

        icons.spacingProperty().bind(spacing);

        box.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 285) {
                spacing.set(Math.max(0, (10 - (285 - newWidth.doubleValue()))));
            } else {
                spacing.set(10);
            }
        }));
    }

    /**
     * Toggle the platform text field to make it un-editable
     * if it's editable and attach text to it, and editable
     * if it's un-editable which will clear the text.
     *
     * @param text Message to display if the text field is
     *             un-editable.
     */
    public void toggle(String text) {
        if (platform.isEditable()) {
            platform.setText(text);
            platform.setEditable(false);
        }
        else {
            platform.setText("");
            platform.setEditable(true);
        }
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the GitHub toggle button is pressed.
     */
    public void handleGithub() {
        toggle("Github");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Google toggle button is pressed.
     */
    public void handleGoogle() {
        toggle("Google");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the shopify toggle button is pressed.
     */
    public void handleShopify() {
        toggle("Shopify");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Discord toggle button is pressed.
     */
    public void handleDiscord() {
        toggle("Discord");
    }
}
