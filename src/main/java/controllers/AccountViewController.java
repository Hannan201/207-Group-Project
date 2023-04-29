package controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import user.Account;
import data.Database;
import user.User;
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
    private GridPane grid;

    @FXML
    private VBox left;

    @FXML
    private TextField search;

    @FXML
    public ListView<Account> accounts;

    @FXML
    private Label defaultText;

    @FXML
    private VBox right;

    @FXML
    private Button select;

    @FXML
    public Button add;

    @FXML
    public Button pin;

    @FXML
    private Button delete;

    @FXML
    public Label title;

    @FXML
    public Button back;

    @FXML
    public Button logout;

    @FXML
    public Button settings;

    private final ObjectProperty<Insets> leftBoxPadding = new SimpleObjectProperty<>(new Insets(0, 0, 0, 30));

    // Don't want the font to be too large.
    private final int MAX_FONT_SIZE = 30;

    private final DoubleProperty gridWidthSize = new SimpleDoubleProperty(530);
    private final DoubleProperty gridHeightSize = new SimpleDoubleProperty(491);

    // To change the font size of the title.
    private final ObjectProperty<Font> titleFontTracking = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the font size needed, of the title.
    private final DoubleProperty titleFontSize = new SimpleDoubleProperty();

    // To dynamically calculate the font size needed, of the buttons.
    private final ObjectProperty<Font> buttonFontSize = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the padding needed, of the
    // buttons to select all, add, pin and delete.
    private final ObjectProperty<Insets> selectPaddingSize = new SimpleObjectProperty<>(new Insets(24.0, 12, 24.0, 12.5));
    private final ObjectProperty<Insets> addPaddingSize = new SimpleObjectProperty<>(new Insets(24.0, 17, 24.0, 17.5));
    private final ObjectProperty<Insets> pinPaddingSize = new SimpleObjectProperty<>(new Insets(24.0, 14, 24.0, 15));
    private final ObjectProperty<Insets> deletePaddingSize = new SimpleObjectProperty<>(new Insets(24.0, 14, 24.0, 14.5));

    private final DoubleProperty spacingSize = new SimpleDoubleProperty(21);

    private final DoubleProperty fieldHeight = new SimpleDoubleProperty(31);

    private final DoubleProperty listViewWidth = new SimpleDoubleProperty(352);
    private final DoubleProperty listViewHeight = new SimpleDoubleProperty(412);

    private final ObjectProperty<Font> noItemsFontSize = new SimpleObjectProperty<>(Font.getDefault());

    private final ObjectProperty<Insets> boxPadding = new SimpleObjectProperty<>(new Insets(40, 5, 0, 5));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.setCellFactory(new AccountCellFactory());
        accounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        search.prefWidthProperty().bind(
                accounts.widthProperty()
        );

        left.paddingProperty().bind(leftBoxPadding);

        box.widthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() < 480) {
                leftBoxPadding.set(new Insets(0, 0, 0, Math.max(0, 30 - (480 - t1.doubleValue()))));
            } else {
                leftBoxPadding.set(new Insets(0, 0, 0, 30));
            }
        });

        titleFontSize.bind(box.widthProperty()
                .add(box.heightProperty())
                .divide(1280 + 720)
                .multiply(100)
                .multiply(20.0 / 58.0)
        );

        box.widthProperty().addListener(((observableValue, number, t1) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            buttonFontSize.set(Font.font(result * 0.75));
            double verticalButtonPadding = result * 1.2;
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.6, verticalButtonPadding, result * 0.625));
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.85, verticalButtonPadding, result * 0.875));
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.7, verticalButtonPadding, result * 0.75));
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.7, verticalButtonPadding, result * 0.725));
            boxPadding.set(new Insets(result * 2, result * 0.25, 0, result * 0.25));
            spacingSize.set(result * 1.05);
            GridPane.setMargin(left, new Insets(0, 0, result * 1.95, 0));
            GridPane.setMargin(right, new Insets(result * 1.9235, 0, result * 1.95, 0));
            fieldHeight.set(result * 1.55);
            listViewWidth.set(result * 17.6);
            listViewHeight.set(result * 20.6);
            noItemsFontSize.set(Font.font(result * 1.25));
            gridWidthSize.set(result * 26.5);
            gridHeightSize.set(result * 24.55);
        }));

        box.heightProperty().addListener(((observableValue, number, t1) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            buttonFontSize.set(Font.font(result * 0.75));
            double verticalButtonPadding = result * 1.2;
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.6, verticalButtonPadding, result * 0.625));
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.85, verticalButtonPadding, result * 0.875));
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.7, verticalButtonPadding, result * 0.75));
            selectPaddingSize.set(new Insets(verticalButtonPadding, result * 0.7, verticalButtonPadding, result * 0.725));
            boxPadding.set(new Insets(result * 2, result * 0.25, 0, result * 0.25));
            spacingSize.set(result * 1.05);
            GridPane.setMargin(left, new Insets(0, 0, result * 1.95, 0));
            GridPane.setMargin(right, new Insets(result * 1.9235, 0, result * 1.95, 0));
            fieldHeight.set(result * 1.55);
            listViewWidth.set(result * 17.6);
            listViewHeight.set(result * 20.6);
            noItemsFontSize.set(Font.font(result * 1.25));
            gridWidthSize.set(result * 26.5);
            gridHeightSize.set(result * 24.55);
        }));

        title.fontProperty().bind(titleFontTracking);

        select.fontProperty().bind(buttonFontSize);
        add.fontProperty().bind(buttonFontSize);
        pin.fontProperty().bind(buttonFontSize);
        delete.fontProperty().bind(buttonFontSize);

        select.paddingProperty().bind(selectPaddingSize);
        add.paddingProperty().bind(addPaddingSize);
        pin.paddingProperty().bind(pinPaddingSize);
        delete.paddingProperty().bind(deletePaddingSize);

        box.paddingProperty().bind(boxPadding);

        GridPane.setMargin(left, new Insets(0, 0, 39, 0));
        GridPane.setMargin(right, new Insets(38.47, 0, 39, 0));

        right.spacingProperty().bind(spacingSize);

        search.prefHeightProperty().bind(fieldHeight);

        accounts.prefWidthProperty().bind(listViewWidth);
        accounts.prefHeightProperty().bind(listViewHeight);

        defaultText.fontProperty().bind(noItemsFontSize);

        grid.prefWidthProperty().bind(gridWidthSize);
        grid.prefHeightProperty().bind(gridHeightSize);
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
        User user = Database.getUser();
        if (user != null) {
            for (Account account : selectedItemsCopy) {
                user.removeAccountByName(account.getName());
            }
        }
    }
}
