package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import user.Account;
import views.utilities.AccountCellFactory;

public class AccountViewController {

    @FXML
    public ListView<Account> accounts;

    @FXML
    public void initialize() {
        accounts.setCellFactory(new AccountCellFactory());
    }
}
