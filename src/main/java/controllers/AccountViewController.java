package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import user.Account;
import views.AddAccountView;
import views.SignUpView;
import views.utilities.AccountCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable {

    @FXML
    public ListView<Account> accounts;

    @FXML
    public Button add;

    @FXML
    public Button pin;

    @FXML
    public Label title;

    @FXML
    public Button back;

    @FXML
    public Button logout;

    @FXML
    public Button settings;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.setCellFactory(new AccountCellFactory());
        ObservableList<Account> data = FXCollections.observableArrayList();
        data.addAll(new Account("mike", "GitGithubGithubGithubGithubissdsadkjdsGithubhub"), new Account("hannan", "Discord"), new Account("Fares", "Shopify"));
        accounts.getItems().addAll(data);
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void handleSelectAccount() {
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accounts.getSelectionModel().selectAll();
    }

    public void handleAddAccount() {


        Stage stage = new Stage();

        if (AddAccountView.getInstance().getRoot().getScene() == null) {
            Scene scene = new Scene(AddAccountView.getInstance().getRoot());
            scene.getStylesheets().add(AddAccountView.getInstance().getCurrentThemePath());
            stage.setScene(scene);
        } else {
            stage.setScene(AddAccountView.getInstance().getRoot().getScene());
        }
        stage.show();

        // this requries the pop up

//        ObservableList<Account> data = FXCollections.observableArrayList();
//        data.add()
//        accounts.getItems().add(new Account())
    }

    public void addAccount(Account account) {
        accounts.getItems().add(account);
    }

    public void handlePinAccount() {

    }

    public void handleDeleteAccount() {

        // stole this from StackOverflow, need to give appropriate credits

        List<Account> selectedItemsCopy = new ArrayList<>(accounts.getSelectionModel().getSelectedItems());
        accounts.getItems().removeAll(selectedItemsCopy);
    }
}
