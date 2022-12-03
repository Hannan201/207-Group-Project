package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import user.Account;
import views.utilities.AccountCellFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable {

    @FXML
    public ListView<Account> accounts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.setCellFactory(new AccountCellFactory());
        ObservableList<Account> data = FXCollections.observableArrayList();
        data.addAll(new Account("mike", "Github"), new Account("hannan", "Discord"), new Account("Fares", "Shopify"));
        accounts.getItems().addAll(data);

    }
}
