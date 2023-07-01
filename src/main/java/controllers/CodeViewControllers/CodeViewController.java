package controllers.CodeViewControllers;

import behaviors.ManualInputReader;
import behaviors.interfaces.ReadCodeBehavior;
import code.readers.CodeReader;
import code.readers.CodeReaderFactory;
import data.Storage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCode;
import data.database.Database;
import models.Account;
import views.*;
import views.interfaces.Reversible;
import models.Code;
import views.utilities.CodeViewUtilities.CodeCellFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class CodeViewController implements Initializable {

    @FXML
    private BorderPane background;

    @FXML
    private VBox titleSection;

    @FXML
    private VBox left;

    @FXML
    private VBox right;

    @FXML
    private Region aboveListView;

    @FXML
    private ListView<Code> codeListView;

    @FXML
    private Label codesTitle;

    @FXML
    private Region aboveButtons;

    @FXML
    private Button importCodes;

    @FXML
    private Button deleteAll;

    @FXML
    public Button addCode;

    @FXML
    private Label title;

    @FXML
    private Label usernameTitle;

    @FXML
    private Region beforeCodeInput;

    @FXML
    private TextField addCodeInput;

    @FXML
    private Region spaceBetween;

    private Account account;

    private final ObjectProperty<Insets> padding = new SimpleObjectProperty<>(new Insets(0, 0, 0, 30));
    private final ObjectProperty<Insets> rightSidePadding = new SimpleObjectProperty<>(new Insets(0, 12, 0, 10));
    private final ObjectProperty<Insets> windowPadding = new SimpleObjectProperty<>(new Insets(5, 5, 5, 5));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.paddingProperty().bind(windowPadding);
        left.paddingProperty().bind(padding);
        right.paddingProperty().bind(rightSidePadding);

        addCodeInput.prefWidthProperty().bind(
                codeListView.widthProperty()
                        .multiply(246.0 / 374.0)
        );

        addCode.prefWidthProperty().bind(
                codeListView.widthProperty()
                        .multiply(96.0 / 374.0)
        );

        spaceBetween.prefWidthProperty().bind(
                codeListView.widthProperty()
                        .multiply(32.0 / 374.0)
        );

        aboveButtons.prefHeightProperty().bind(
                codesTitle.heightProperty()
                        .add(aboveListView.heightProperty())
                        .add(34)
        );

        background.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            if (newHeight.doubleValue() < 318) {
                double newPadding = Math.max(0, 5 - (318 - newHeight.doubleValue()));
                windowPadding.set(new Insets(newPadding, 5, newPadding, 5));
                BorderPane.setMargin(titleSection, new Insets(Math.max(0, 10 - (318 - newHeight.doubleValue())), 0, 0, 0));
            } else {
                windowPadding.set(new Insets(5, 5, 5, 5));
                BorderPane.setMargin(titleSection, new Insets(10, 0, 0, 0));
            }
        }));

        background.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 480) {
                padding.set(new Insets(0, 0, 0, Math.max(0, 30 - (480 - newWidth.doubleValue()))));
                beforeCodeInput.prefWidthProperty().unbind();
                beforeCodeInput.setPrefWidth(padding.getValue().getLeft());

                if (newWidth.doubleValue() < 261) {
                    double newPadding = Math.max(0, 5 - (261 - newWidth.doubleValue()));
                    windowPadding.set(new Insets(newPadding, newPadding, newPadding, newPadding));

                    double newMargin = Math.max(0, 10 - (261 - newWidth.doubleValue()));
                    BorderPane.setMargin(titleSection, new Insets(newMargin, 0, 0, 0));
                    titleSection.setPrefHeight(57 + 200 - newWidth.doubleValue());

                    if (newWidth.doubleValue() < 221) {
                        double delta = 221 - newWidth.doubleValue();
                        rightSidePadding.set(new Insets(0, Math.max(0, 12 - delta), 0, Math.max(0, 10 - delta)));
                    } else {
                        rightSidePadding.set(new Insets(0, 12, 0, 10));
                    }
                } else {
                    windowPadding.set(new Insets(5, 5, 5, 5));
                    BorderPane.setMargin(titleSection, new Insets(10, 0, 0, 0));
                    titleSection.setPrefHeight(57);
                }
            } else {
                padding.set(new Insets(0, 0, 0, 30));
                beforeCodeInput.setPrefWidth(30);
            }
        }));

        codeListView.setCellFactory(test -> {
            try {
                return new CodeCellFactory(codeListView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Handles the event where the "Import Codes" button is clicked
     * This method allows the user to specify the path to the corresponding
     * .txt file that contains the codes they would like to add.
     */
    public void importCodeOnAction() {
        // 1) Get the platform for the account
        String AccountType = account.getSocialMediaType().toLowerCase();

        // 2) select a reader based on the corresponding account type
        CodeReader reader = CodeReaderFactory.makeCodeReader(AccountType);

        // 3) open the file explorer
        FileChooser chooser = new FileChooser();
        File pathway = chooser.showOpenDialog(CodeView.getInstance().getRoot().getScene().getWindow());

        // 4) get the path to the specified file
        if (!(pathway == null)) {
            if (reader != null) {
                String filepath = pathway.getPath();

                // 5) Read the file using the corresponding reader
                reader.setFilePath(filepath);
                addCodes((ReadCodeBehavior) reader);
            }
        }
    }

    /**
     * Handles the event when the "delete all codes" button is clicked.
     * This method clears the items in the listview.
     */
    public void deleteAllOnAction() {
        codeListView.getItems().clear();
        Database.clearAllCodes(Storage.getToken(), account.getID());
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code to the listview.
     */
    public void addCodeOnAction() {
        addCodes(new ManualInputReader(addCodeInput));
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code using the enter button.
     */
    public void addCodeOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addCodes(new ManualInputReader(addCodeInput));
        }
    }

    /**
     * Add list of codes based on the behavior.
     *
     * @param behavior Obtains a backup code depending on the source.
     */
    private void addCodes(ReadCodeBehavior behavior) {
        List<String> returned = behavior.readCodes();
        for (String s : returned) {
            // Don't want to add empty codes.
            if (!s.isEmpty()) {
                int id = Database.addCode(Storage.getToken(), account.getID(), s);
                codeListView.getItems().add(Database.getCode(Storage.getToken(), id));
                addCodeInput.setText("");
            }
        }
    }

    /**
     * Clear the list of codes being displayed in this view
     * and only show the codes for a specific social media
     * account.
     *
     * @param ID ID of the social media account to show
     *             the codes for.
     */
    public void addCodes(int ID) {
        codeListView.getItems().clear();
        account = Database.getAccount(Storage.getToken(), ID);
        if (account != null) {
            title.setText(account.getSocialMediaType() + "\n");
            usernameTitle.setText(account.getName());

            // Only allow user to click the import codes button
            // if their account is supported.
            importCodes.setDisable(!Account.supportsImport(account.getSocialMediaType().toLowerCase()));

            List<Code> codes = Database.getCodes(Storage.getToken(), ID);
            for (Code c : codes) {
                codeListView.getItems().add(c);
            }
        }
    }

    /**
     * Return the Account that's currently
     * being used to display the codes.
     *
     * @return Account object which is currently
     * is in use.
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * Allows a user to logout and redirects them to the home page.
     */
    public void handleLogout() {
        Database.logUserOut(Storage.getToken());
        View.switchSceneTo(CodeView.getInstance(), HomePageView.getInstance());
        Storage.setToken(null);
    }

    /**
     * Switches the scene to the SettingsView once the settings button is clicked.
     */
    public void handleSettings() {
        ((Reversible) SettingsView.getInstance()).setPreviousView(CodeView.getInstance());
        View.switchSceneTo(CodeView.getInstance(), SettingsView.getInstance());
    }

    /**
     * Switches the scene to the Account-Viewer view once the back button is clicked.
     */
    public void handleGoBack() {
        View.switchSceneTo(CodeView.getInstance(), AccountView.getInstance());
    }
}
