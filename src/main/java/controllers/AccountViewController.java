package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import user.Account;
import user.Database;
import user.User;
import views.*;
import views.utilities.AccountCell;
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

    // Will be needed for the final product.
//    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.setCellFactory(new AccountCellFactory());
        ObservableList<Account> data = FXCollections.observableArrayList();

        // These two lines don't work for now.
        // But when the loading data feature
        // is added, it will.
//        User user = Database.getUser();
//        data.addAll(user.getAccounts());

        data.addAll(new Account("mike", "GitGithubGithubGithubGithubissdsadkjdsGithubhub"), new Account("hannan", "Discord"), new Account("Fares", "Shopify"));
        accounts.getItems().addAll(data);
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
     * Responsible for switching views.
     */
    private void switchSceneTo(View view) {
        Scene scene = AccountView.getInstance().getRoot().getScene();
        scene.getStylesheets().clear();

        // Just in case if CSS files aren't being used
        // to change the theme.
        if (view.getCurrentThemePath() != null) {
            scene.getStylesheets().add(view.getCurrentThemePath());
        }

        scene.setRoot(view.getRoot());
    }

    /**
     * A handle method for the logout button which saves the user data and changes the current view
     * to the HomePageView.
     */
    public void handleLogout(ActionEvent e) {
        // This saveUserData method doesn't do
        // anything for now, but it will once
        // the final product is ready.

        Database.saveUserData();
        switchSceneTo(HomePageView.getInstance());
    }

    /**
     * A handle method for the settings button which changes the current view to the SettingsView.
     */
    public void handleSettings(ActionEvent e) {
        switchSceneTo(SettingsView.getInstance());
    }

    /**
     * A handle method for the add button.
     *
     * Generates a new AddAccount pop up where the user can
     * create an account and have it reflected in the AccountView.
     */
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

    /**
     * Adds an account to the accounts ListView.
     */
    public void addAccount(Account account) {
        accounts.getItems().add(account);
    }

    /**
     * Checks if there exists a duplicate account in the
     * accounts ListView.
     *
     * @param compte
     * @return if there exists a duplicate account in the list
     */
    public boolean existsDuplicate(Account compte) {

        for (Account account : accounts.getItems()) {
            if (compte.equals(account)) {
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
