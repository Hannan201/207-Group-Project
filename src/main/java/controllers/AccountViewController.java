package controllers;

import controllers.utilities.Debouncer;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import models.Account;
import data.Database;
import models.User;
import views.*;
import views.interfaces.Reversible;
import views.utilities.AccountCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        box.sceneProperty().addListener(
                (observableValue, oldScene, newScene) -> {
                    if (oldScene == null && newScene != null) {
                        newScene.windowProperty().addListener(
                                (observableValue1, oldWindow, newWindow) -> {
                                    if (oldWindow == null && newWindow != null) {
                                        newWindow.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (windowEvent) -> {
                                            debounce.tearDown();
                                        });
                                    }
                                }
                        );
                    }
                }
        );

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
     *
     * @param keyEvent The key event.
     */
    @FXML
    private void handleSearchRelease(KeyEvent keyEvent) {
        User user = Database.getUser();
        if (user != null) {
            if (search.getText().isEmpty()) {
                debounce.registerFunction(
                        "DEFAULT",
                        () -> {
                            Platform.runLater(() -> {
                                accounts.getItems().clear();
                                accounts.getItems().addAll(user.getAccounts());
                            });

                            return null;
                        },
                        125
                );
                return;
            }

            debounce.registerFunction(
                    search.getText(),
                    () -> {
                        Platform.runLater(() -> {
                            List<Account> result = user.searchAccounts(
                                    search.getText()
                            );

                            accounts.getItems().clear();
                            accounts.getItems().addAll(result);
                        });
                        return null;
                    },
                    125
            );
        }
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
        User user = Database.getUser();
        if (user != null) {
            for (Account account : selectedItemsCopy) {
                user.removeAccountByName(account.getName());
            }
        }
    }
}
