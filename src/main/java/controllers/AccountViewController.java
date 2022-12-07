package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import user.Account;
import data.Database;
import views.*;
import views.interfaces.Reversible;
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
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Selects all accounts in the accounts ListView.
     */
    public void handleSelectAccount() {
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accounts.getSelectionModel().selectAll();
    }

    /**
     * A handle method for the logout button which saves the user data and changes the current view
     * to the HomePageView.
     */
    public void handleLogout() {
        Database.logUserOut();
        View.switchSceneTo(AccountView.getInstance(), HomePageView.getInstance());
    }

    /**
     * A handle method for the settings button which changes the current view to the SettingsView.
     */
    public void handleSettings() {
        ((Reversible) SettingsView.getInstance()).setPreviousView(AccountView.getInstance());
        View.switchSceneTo(AccountView.getInstance(), SettingsView.getInstance());
    }

    /**
     * A handle method for the add button.
     * -
     * Generates a new AddAccount pop up where the user can
     * create an account and have it reflected in the AccountView.
     */
    public void handleAddAccount() {
        View.loadNewWindow(AddAccountView.getInstance());
    }

    /**
     * Adds an account to the accounts ListView.
     *
     * @param account The Account which is to be added.
     */
    public void addAccount(Account account) {
        accounts.getItems().add(account);
        if (Database.getUser() != null) {
            Database.getUser().addNewAccount(account);
        }
    }

    /**
     * Add multiple accounts to the ListView.
     *
     * @param userAccounts List containing the accounts.
     */
    public void addAccounts(List<Account> userAccounts) {
        accounts.getItems().clear();
        accounts.getItems().addAll(userAccounts);
    }

    /**
     * Checks if there exists a duplicate account in the
     * accounts ListView.
     *
     * @param compare The Account to search for.
     * @return if there exists a duplicate account in the list
     */
    public boolean existsDuplicate(Account compare) {
        for (Account account : accounts.getItems()) {
            if (compare.equals(account)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A handle method for the <pin accounts> button.
     */
    public void handlePinAccount() {

    }

    /**
     * A handle method for the <delete accounts> button which deletes all selected accounts in the accounts ListView.
     *
     */
    public void handleDeleteAccount() {

        // Credit to https://stackoverflow.com/questions/24206854/javafx-clearing-the-listview for the code below.

        List<Account> selectedItemsCopy = new ArrayList<>(accounts.getSelectionModel().getSelectedItems());
        accounts.getItems().removeAll(selectedItemsCopy);
    }
}
