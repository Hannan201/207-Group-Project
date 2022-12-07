package controllers.CodeViewControllers;

import code.readers.CodeReader;
import code.readers.CodeReaderFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import user.Database;
=======
import javafx.scene.input.KeyCode;
import data.Database;
import views.*;
import views.utilities.CodeViewUtilities.CodeCell;
import views.utilities.CodeViewUtilities.CodeCellFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;



public class CodeViewController implements Initializable {

    @FXML
    private BorderPane background;

    @FXML
    private Label placeholderText;
    @FXML
    private ListView<CodeCell> CodeListView;

    @FXML
    private Label codesTitle;
    @FXML
    private Button importCodes; //To be implemented in upcoming sprint

    @FXML
    private Button deleteAll;

    @FXML
    public Button addCode;

    @FXML
    private TextField addCodeInput;

    // TODO uncomment this line once the account can be referenced
    // private Account currAccount = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CodeListView.setCellFactory(test -> {
            try {
                return new CodeCellFactory(CodeListView);
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
        importCodes.setOnAction(event -> {
            // TODO uncomment this lone once the account can be referenced.
            // if (curAccount == null ) {;}
            // else {}

            // 1) retrieve the corresponding account type
            // TODO for testing purposes, assume the SocialMediaType is Shopify

            String AccountType = "shopify";

            // TODO uncomment this line once the account can be referenced:
            //  String AccountType = currAccount.getSocialMediaType();

            // 2) select a reader based on the corresponding account type
            CodeReader reader = CodeReaderFactory.makeCodeReader(AccountType);

            // 3) open the file explorer
            FileChooser chooser = new FileChooser();
            File pathway = chooser.showOpenDialog(CodeView.getInstance().getRoot().getScene().getWindow());

            // 4) get the path to the specified file
            //TODO for testing purposes, assume the path refers to a Shopify codes file.
            if (!(pathway == null)) {
                String filepath = pathway.getPath();

                // 5) Read the file using the corresponding reader

                List<String> importedCodes = reader.extractCodes(filepath);

                // 6) Take the List<Strings> that is returned and turn them into CodeCell Objects, which can
                //    be added into the list view.
                for (String code : importedCodes) {
                    CodeListView.getItems().add(new CodeCell(code));
                }
            }
        });
    }

    /**
     * Handles the event when the "delete all codes" button is clicked.
     * This method clears the items in the listview.
     */
    public void deleteAllOnAction() {
        deleteAll.setOnAction(e -> {
            CodeListView.getItems().clear();
        });
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code to the listview.
     */
    public void addCodeOnAction() {
        addCode.setOnAction(event -> {
            String newItem = addCodeInput.getText();
            CodeListView.getItems().add(new CodeCell(newItem));
            addCodeInput.setText("");
        });
    }

    /**
     * Hanldes the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code using the enter button.
     */
    public void addCodeOnEnter() {
        addCodeInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newItem = addCodeInput.getText();
                CodeListView.getItems().add(new CodeCell(newItem));
                addCodeInput.setText("");
            }
        });
    }

    /**
     * Set the currAccount attribute to the specified account.
     *
     */
    // public void setAccount(Account account) {
    //     currAccount = account;
    // }

    private void switchSceneTo(View view) {
        Scene scene = CodeView.getInstance().getRoot().getScene();
        scene.getStylesheets().clear();

        // Just in case if CSS files aren't being used
        // to change the theme.
        if (view.getCurrentThemePath() != null) {
            scene.getStylesheets().add(view.getCurrentThemePath());
        }

        scene.setRoot(view.getRoot());
    }

    /**
     * Allows a user to logout and redirects them to the home page.
     *
     */
    public void handleLogout(ActionEvent e) {
        // This saveUserData method doesn't do
        // anything for now, but it will once
        // the final product is ready.

        Database.saveUserData();
        switchSceneTo(HomePageView.getInstance());
    }

    /**
     * Switches the scene to the SettingsView once the settings button is clicked.
     *
     */
    public void handleSettings(ActionEvent e) {
        switchSceneTo(SettingsView.getInstance());
    }

    /**
     * Shows the AddAccountView scene to allow the user to add an account
     * once the add button is clicked.p
     *
     */
}
