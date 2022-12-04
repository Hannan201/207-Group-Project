package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.ValidationMessage;
import net.synedra.validatorfx.Validator;
import org.w3c.dom.Text;
import user.Account;
import views.AccountView;
import views.AddAccountView;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController {

    private Validator validator = new Validator();
    private StringBinding problemsText;


    @FXML
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

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ToggleGroup group = new ToggleGroup();
//        github.setToggleGroup(group);
//        google.setToggleGroup(group);
//        shopify.setToggleGroup(group);
//        discord.setToggleGroup(group);
//        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
//                createAccount,
//                validator.containsErrorsProperty(),
//                Bindings.concat("Cannot add account:\n", validator.createStringBinding()));
//
//
//    }
    public void handleCreateAccount(ActionEvent e) {

//        createAccountWrapper


        Account account = new Account(username.getText(), platform.getText());
        ((AccountView)AccountView.getInstance()).getAccountViewController().addAccount(account);
        Stage stage = (Stage) AddAccountView.getInstance().getRoot().getScene().getWindow();
        stage.close();

        // need to use tooltip to ensure that everything is the same...
    }

}
