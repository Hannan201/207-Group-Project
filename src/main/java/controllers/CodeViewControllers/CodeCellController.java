package controllers.CodeViewControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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

    /**
     * Stores the data of the current code, the code factory, and its corresponding listview;
     *
     * @param cell The current CodeCell
     * @param container The CodeCellFactory for this CodeCell
     * @param parent The ListView that contains this CodeCell
     */
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

    /**
     * Handles the selection of a particular cell so that the user gains access
     * to the copy, edit and delete buttons.
     */
    public void layoutOnEnter() {
        backgroundLayout.setOnMouseClicked(e -> {
            if (selected) {
                // in the case where the
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

    /**
     * Handle when the edit button is clicked.
     */
    public void editOnAction(){
        edit.setOnAction(e -> {

            //Disable interactivity with other buttons to prevent overlapping actions
            delete.setDisable(true);
            copy.setDisable(true);

            userInput.setText(code.getText());

            // swap the visibility of the label and the textfield so that the user can edit the code name.
            userInput.setVisible(true);
            code.setVisible(false);


        });
    }

    /**
     * Handles when the enter button is clicked whlie the textfield is selected
     * This method will submit edit changes.
     */
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
        // After the edit operation is finished, the delete and copy buttons can be interacted with.
        delete.setDisable(false);
        copy.setDisable(false);
    }

    /**
     * Handles when the copy button is pressed
     */
    public void copyOnAction(){

        copy.setOnAction(e -> {

            //Get the system clipboard so the code be copied accordingly
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();

            content.putString(currentCell.getCode());
            clipboard.setContent(content);
        });
    }

    /**
     * Handles when the delete button is pressed
     */
    public void deleteOnAction() {

        // handle the event when delete button is clicked
        delete.setOnAction(e -> {
            currentListView.getItems().remove(currentCell);
        });
    }

    /**
     * Setter for the code attribute.
     * @param codeInput the code to be set.
     */
    public void setCode(String codeInput) {
        this.code.setText(codeInput);
    }

}
