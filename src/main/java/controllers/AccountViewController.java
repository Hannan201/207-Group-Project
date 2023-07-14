package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Debouncer;
import data.Storage;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Account;
import data.database.Database;
import views.*;
import views.interfaces.Reversible;
import views.utilities.AccountCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(AccountViewController.class);

    @FXML
    private BorderPane box;

    @FXML
    private VBox left;

    @FXML
    private TextField search;

    @FXML
    private Region space;

    @FXML
    public ListView<Account> accounts;

    @FXML
    private Region bottomListView;

    @FXML
    private Region top;

    @FXML
    public Button add;

    @FXML
    public Button pin;

    @FXML
    private Region bottomButtons;

    @FXML
    public Label title;

    @FXML
    public Button back;

    @FXML
    public Button logout;

    @FXML
    public Button settings;

    private final ObjectProperty<Insets> padding = new SimpleObjectProperty<>(new Insets(40, 5, 0 ,5));

    private final ObjectProperty<Insets> leftBoxPadding = new SimpleObjectProperty<>(new Insets(0, 0, 0, 30));

    // To make searching more efficient.
    private Debouncer debounce;

    /**
     * Configure the stage for this view to disconnect and close threads
     * on shut down.
     *
     * @param stage Stage of the application.
     */
    public void configureStage(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> {
            debounce.tearDown();
            Database.disconnect();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.setCellFactory(new AccountCellFactory());
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        top.prefHeightProperty().bind(
                search.heightProperty()
                        .add(space.heightProperty())
        );

        bottomButtons.prefHeightProperty().bind(
                bottomListView.heightProperty()
        );

        search.prefWidthProperty().bind(
                accounts.widthProperty()
        );

        box.paddingProperty().bind(padding);
        left.paddingProperty().bind(leftBoxPadding);

        box.widthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() < 480) {
                leftBoxPadding.set(new Insets(0, 0, 0, Math.max(0, 30 - (480 - t1.doubleValue()))));
            } else {
                leftBoxPadding.set(new Insets(0, 0, 0, 30));
            }
        });

        box.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            if (newHeight.doubleValue() < 594) {
                padding.set(new Insets(Math.max(0, 40 - 594 + newHeight.doubleValue()), 5, 0, 5));

                if (newHeight.doubleValue() < 570) {
                    BorderPane.setAlignment(title, Pos.CENTER);

                    if (newHeight.doubleValue() < 395) {
                        space.setMinHeight(Region.USE_COMPUTED_SIZE);
                    } else {
                        space.setMinHeight(space.getPrefHeight());
                    }
                } else {
                    BorderPane.setAlignment(title, Pos.BASELINE_CENTER);
                }
            } else {
                padding.set(new Insets(40, 5, 0, 5));
            }
        }));

        debounce = new Debouncer();
    }


    /**
     * Handle method for when a key is released
     * on the text field to search for an
     * account.
     */
    @FXML
    private void handleSearchRelease() {
        String result = search.getText();
        String key = result.isEmpty() ? "DEFAULT" : result;

        logger.debug("Setting search query for account with name {}", key);
        debounce.registerFunction(
                key,
                () -> {
                    Platform.runLater(() -> {
                        List<Account> allAccounts = Database.getAccounts(Storage.getToken());

                        if (!result.isEmpty()) {
                            allAccounts = searchAccounts(
                                    allAccounts, key
                            );
                        }

                        accounts.getItems().clear();
                        accounts.getItems().addAll(allAccounts);

                        logger.debug("Finished search for account with name {}.", key);
                    });

                    return null;
                },
                125
        );
    }

    /**
     * Utility method to search through the list of accounts and find all
     * accounts that have a username that matches the name to search.
     * <p>
     * In this case, match means that the name being searched
     * is contained in the account name.
     * <p>
     * Both the account name and the name being searched are converted to
     * lowercase before the contains-check is done.
     *
     * @param accounts List of accounts.
     * @param name The name of the account to search.
     * @return List of accounts with usernames that match.
     */
    public static List<Account> searchAccounts(List<Account> accounts, String name) {
        logger.debug("Starting search for account with name: {}.", name);

        return accounts.stream().filter(account -> account.getName()
                .toLowerCase()
                .contains(name.toLowerCase()))
                .toList();
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
        Database.logUserOut(Storage.getToken());

        logger.trace("Switching from the AccountView to the HomePageView.");
        View.switchSceneTo(AccountView.getInstance(), HomePageView.getInstance());

        Storage.setToken(null);
        accounts.getItems().clear();
    }

    /**
     * A handle method for the settings button which changes the current view to the SettingsView.
     */
    public void handleSettings() {
        logger.debug("Setting the previous scene for the SettingsView to the AccountView.");
        ((Reversible) SettingsView.getInstance()).setPreviousView(AccountView.getInstance());

        logger.trace("Switching from the AccountView to the SettingsView.");
        View.switchSceneTo(AccountView.getInstance(), SettingsView.getInstance());
    }

    /**
     * A handle method for the add button.
     * -
     * Generates a new AddAccount pop up where the user can
     * create an account and have it reflected in the AccountView.
     */
    public void handleAddAccount() {
        logger.trace("Engaging CreateAccountView window.");
        View.loadNewWindow(AddAccountView.getInstance());
    }

    /**
     * Adds an account to the accounts ListView.
     *
     * @param ID ID of the Account to be added.
     */
    public void addAccount(int ID) {
        accounts.getItems()
                .add(
                        Database.getAccount(Storage.getToken(), ID)
                );
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
        for (Account account : selectedItemsCopy) {
            Database.removeAccount(Storage.getToken(), account.getID());
        }
    }
}
