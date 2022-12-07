package controllers.CodeViewControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import user.Database;
import views.*;
import views.utilities.CodeViewUtilities.CodeCell;
import views.utilities.CodeViewUtilities.CodeCellFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CodeViewController implements Initializable {

    @FXML
    private ListView<CodeCell> TestListView;

    @FXML
    private Button importCodes; //To be implemented in upcoming sprint

    @FXML
    private Button deleteAll;

    @FXML
    public Button addCode;

    @FXML
    private TextField addCodeInput;


    private ObservableList<CodeCell> TestList;

    public CodeViewController() {
        TestList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TestListView.getItems().addAll(TestList);
        TestListView.setCellFactory(test -> {
            try {
                return new CodeCellFactory(TestListView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void importCodeOnAction() {
    }

    public void deleteAllOnAction() {
        deleteAll.setOnAction(e -> {
            TestListView.getItems().clear();
        });

    }

    public void addCodeOnAction() {
        addCode.setOnAction(event -> {
            String newItem = addCodeInput.getText();
            TestListView.getItems().add(new CodeCell(newItem));
        });
    }


    public void addCodeOnEnter() {
        addCodeInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newItem = addCodeInput.getText();
                TestListView.getItems().add(new CodeCell(newItem));
            }
        });
    }

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
     * once the add button is clicked.
     *
     */
}
