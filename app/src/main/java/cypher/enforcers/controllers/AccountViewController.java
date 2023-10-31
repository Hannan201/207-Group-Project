package cypher.enforcers.controllers;

import cypher.enforcers.data.security.dtos.Account;
import cypher.enforcers.models.AccountModel;
import cypher.enforcers.models.UserModel;
import cypher.enforcers.views.accountview.AccountView;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Debouncer;
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
import cypher.enforcers.data.entities.AccountEntity;
import cypher.enforcers.views.*;
import cypher.enforcers.views.interfaces.Reversible;
import cypher.enforcers.views.accountview.AccountCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the account view.
 */
public class AccountViewController implements Initializable {

    // Logger for the account view.
    private static final Logger logger = LoggerFactory.getLogger(AccountViewController.class);

    // Container for all the elements in this view.
    @FXML
    private BorderPane box;

    // Title for this view.
    @FXML
    public Label title;

    // Contains the search text field and the list-view to be displayed
    // on top of each other.
    @FXML
    private VBox left;

    // Text field to search for accounts.
    @FXML
    private TextField search;

    // Controls the spacing between the text field and
    // list-view.
    @FXML
    private Region space;

    // Displays all the accounts.
    @FXML
    public ListView<Account> accounts;

    // Controls the spacing above the add button. So that it's inline
    // with the accounts view.
    @FXML
    private Region top;

    // Button that says Add Account.
    @FXML
    public Button add;

    // Button that says Pin Account.
    @FXML
    public Button pin;

    // Contains the three buttons at the bottom to be displayed
    // horizontally.
    @FXML
    private HBox bottomButtons;

    // Button to go back to the previous view.
    @FXML
    public Button back;

    // Button to logout.
    @FXML
    public Button logout;

    // Button to go to the settings.
    @FXML
    public Button settings;

    // This property is used to control the padding of the main
    // container so that the contents can fit if the screen gets too
    // small.
    private final ObjectProperty<Insets> padding = new SimpleObjectProperty<>(new Insets(40, 5, 0 ,5));

    // This property is used to control the padding on the right side
    // of the list-view so that the content can fit when the screen
    /// gets too small.
    private final ObjectProperty<Insets> leftBoxPadding = new SimpleObjectProperty<>(new Insets(0, 0, 0, 30));

    // This property controls the spacing below the list-view so that
    // the content can fit when the screen gets too small.
    private final ObjectProperty<Insets> belowPadding = new SimpleObjectProperty<>(new Insets(38, 0, 0, 0));

    // To make searching accounts more efficient.
    private Debouncer debounce;

    // To interact with the current user.
    private UserModel userModel;

    // To interact with the user's accounts.
    private AccountModel accountModel;

    public void setUserModel(UserModel model) {
        this.userModel = model;
    }

    public void setAccountModel(AccountModel model) {
        this.accountModel = model;
    }

    /**
     * Configure the stage for this view to disconnect and close threads
     * on shut down.
     *
     * @param stage Stage of the application.
     */
    public void configureStage(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> {
            debounce.tearDown();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.setCellFactory(new AccountCellFactory());
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accounts.itemsProperty().bind(accountModel.accountsProperty());

        if (!Objects.isNull(userModel.getCurrentUser())) {
            accountModel.loadAccounts(userModel.getCurrentUser().id());
        }

        userModel.currentUserProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.isNull(newValue)) {
                accountModel.loadAccounts(newValue.id());
                return;
            }

            accountModel.clear();
        });

        accountModel.currentAccountProperty().bind(accounts.getSelectionModel().selectedItemProperty());

        /*
        This view would break down if the screen went too large or
        small. I'm not good at UI thus I didn't know how to make it look,
        so for now I just made it look "functional" when the screen
        sizes change. if screen size is the default width and height, then
        I did my best to maintain the spacings that were there when
        this project was submitted, since I didn't want to tamper the UI.
        You are fully free to change these bindings.
         */

        // This makes it so the top of the Select All Accounts button
        // is inline with the top of the list-view.
        top.prefHeightProperty().bind(
                search.heightProperty()
                        .add(space.heightProperty())
        );

        // This makes it so the width of the text field and the
        // list-view are the same.
        search.prefWidthProperty().bind(
                accounts.widthProperty()
        );

        box.paddingProperty().bind(padding);
        left.paddingProperty().bind(leftBoxPadding);
        bottomButtons.paddingProperty().bind(belowPadding);

        box.widthProperty().addListener((observableValue, number, t1) -> {
            // When the window's width becomes smaller than 480 pixels
            // (value found from trial and error), then adjust the
            // padding so there's more room for the content.
            if (t1.doubleValue() < 480) {
                leftBoxPadding.set(new Insets(0, 0, 0, Math.max(0, 30 - (480 - t1.doubleValue()))));
            } else {
                leftBoxPadding.set(new Insets(0, 0, 0, 30));
            }
        });

        box.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            /*
            There are multiple breakpoints I found from trial and error
            and if the width gets smaller than those breakpoints, then
            paddings will adjust so there's more room for the content.
             */
            if (newHeight.doubleValue() < 594) {
                padding.set(new Insets(Math.max(0, 40 - 594 + newHeight.doubleValue()), 5, 0, 5));
                belowPadding.set(new Insets(Math.max(0, 38 - 594 + newHeight.doubleValue()), 0, 0, 0));

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
                belowPadding.set(new Insets(38, 0, 0, 0));
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
                        if (!result.isEmpty()) {
                            accounts.itemsProperty().unbind();
                            accounts.setItems(FXCollections.observableList(findAccounts(accountModel.getAccounts(), key)));
                        } else {
                            accounts.itemsProperty().bind(accountModel.accountsProperty());
                        }

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
    public static List<Account> findAccounts(List<Account> accounts, String name) {
        logger.debug("Starting search for account with name: {}.", name);

        return accounts.stream().filter(account -> account.name()
                        .toLowerCase()
                        .contains(name.toLowerCase()))
                .toList();
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
    public static List<AccountEntity> searchAccounts(List<AccountEntity> accounts, String name) {
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
        if (userModel.logOutUser()) {
            logger.trace("Switching from the AccountView to the HomePageView.");
            View.switchSceneTo(AccountView.getInstance(), HomePageView.getInstance());
        }
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
//        accounts.getItems()
//                .add(
//                        Database.getAccount(Storage.getToken(), ID)
//                );
    }

    /**
     * Add multiple accounts to the ListView.
     *
     * @param userAccounts List containing the accounts.
     */
    public void addAccounts(List<AccountEntity> userAccounts) {
        accounts.getItems().clear();
        //accounts.getItems().addAll(userAccounts);
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
        accountModel.deleteAccounts(userModel.getCurrentUser().id(), selectedItemsCopy);
    }
}
