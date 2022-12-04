package controllers.CodeViewControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
}
