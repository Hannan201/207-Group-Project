package controllers.CodeViewControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import views.utilities.CodeViewUtilities.CodeCell;
import views.utilities.CodeViewUtilities.CodeCellFactory;


public class CodeCellController {
    @FXML
    private Label code;

    @FXML
    private TextField userInput;

    @FXML
    private Button copy;

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private HBox backgroundLayout;

    private CodeCell currentCell;

    private CodeCellFactory currentContainer;
    private ListView<CodeCell> currentListView;

    private boolean selected;

    public void setCodeCell(CodeCell cell, CodeCellFactory container, ListView<CodeCell> parent) {
        currentCell = cell;
        currentContainer = container;
        currentListView = parent;
        selected = false;

        //Making the text of the code and text work in tandem with one another.
        code.setText(cell.getCode());
        userInput.setText(cell.getCode());

        edit.setDisable(true);
        delete.setDisable(true);
        copy.setDisable(true);

        userInput.setVisible(false);
    }

    public void layoutOnEnter() {
        backgroundLayout.setOnMouseClicked(e -> {
            if (selected) {
                edit.setDisable(false);
                delete.setDisable(false);
                copy.setDisable(false);

                selected = false;
            } else{
                edit.setDisable(true);
                delete.setDisable(true);
                copy.setDisable(true);
                selected = true;
            }
        });
    }

    public void editOnAction(){

        // Handle when the edit button is clicked.
        edit.setOnAction(e -> {
            delete.setDisable(true);
            copy.setDisable(true);

            userInput.setText(code.getText());

            // swap the visibility of the label and the textfield so that the user can edit the code name.
            userInput.setVisible(true);
            code.setVisible(false);


        });
    }

    public void TextFieldOnAction() {

        // handle the event when enter is pressed. This is done so that the user submit their edit more feasibly
        userInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                code.setText((userInput.getText()));
                currentCell.setCode(userInput.getText());

                userInput.setVisible(false);
                code.setVisible(true);
            }
        });
        delete.setDisable(false);
        copy.setDisable(false);
    }


    public void copyOnAction(){

        // Handle when the edit button is clicked.
        copy.setOnAction(e -> {

            // Add the new cell right below the selected one so the user can immediately see the copy they made
            CodeCell item = new CodeCell(currentCell.getCode());
            int index = currentListView.getItems().indexOf(currentCell);

            currentListView.getItems().add(index + 1, item);
        });
    }

    public void deleteOnAction() {

        // handle the event when delete button is clicked
        delete.setOnAction(e -> {
            currentListView.getItems().remove(currentCell);
        });
    }

    public void setCode(String codeInput) {
        this.code.setText(codeInput);
    }

    public void setCode(Label code) {
        this.code = code;
    }

}
