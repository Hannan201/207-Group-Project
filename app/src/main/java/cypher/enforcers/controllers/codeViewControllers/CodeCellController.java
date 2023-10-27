package cypher.enforcers.controllers.codeViewControllers;

import cypher.enforcers.data.database.Database;
import cypher.enforcers.data.Storage;
import cypher.enforcers.models.CodeModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import cypher.enforcers.code.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for the code cell.
 */
public class CodeCellController {

    // Logger for the code cell controller.
    private static final Logger logger = LoggerFactory.getLogger(CodeCellController.class);

    // Label that displays the code.
    @FXML
    private Label code;

    // Text field used to update the code.
    @FXML
    private TextField userInput;

    // Button that says Copy.
    @FXML
    private Button copy;

    // Button that says Delete.
    @FXML
    private Button delete;

    // Button that says Edit.
    @FXML
    private Button edit;

    // Reference to the current code associated with this cell.
    private Code currentCell;

    // Reference to the parent list view.
    private ListView<Code> currentListView;

    // If this code is selected or not.
    private boolean selected;

    // Used to interact with the account's codes.
    private CodeModel codeModel;

    /**
     * Stores the data of the current code, the code factory, and its corresponding listview.
     *
     * @param cell The current CodeCell
     * @param parent The ListView that contains this CodeCell
     */
    public void setCodeCell(Code cell, ListView<Code> parent) {
        currentCell = cell;
        currentListView = parent;
        selected = false;

        // Making the text of the code and text work in tandem with one another.
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
        if (selected) {
            // in the case where the cell isn't clicked
            edit.setDisable(false);
            delete.setDisable(false);
            copy.setDisable(false);

            selected = false;
        } else {
            edit.setDisable(true);
            delete.setDisable(true);
            copy.setDisable(true);
            selected = true;
        }
    }

    /**
     * Handle when the edit button is clicked.
     */
    public void editOnAction() {
        // Disable interactivity with other buttons to prevent overlapping actions
        delete.setDisable(true);
        copy.setDisable(true);

        userInput.setText(code.getText());

        // swap the visibility of the label and the TextField so that the user can edit the code name.
        userInput.setVisible(true);
        code.setVisible(false);
    }

    /**
     * Handles when the enter button is clicked while the TextField is selected
     * This method will submit edit changes.
     */
    public void TextFieldOnAction(KeyEvent e) {
        // handle the event when enter is pressed. This is done so that the user submit their edit more feasibly
        // Don't let the user enter an empty code.
        if (e.getCode() == KeyCode.ENTER && !userInput.getText().isEmpty()) {
            logger.debug("Switching code from {} to {}.", currentCell.getCode(), userInput.getText());

            Database.updateCode(Storage.getToken(), (int) currentCell.getId(), userInput.getText());

            code.setText((userInput.getText()));
            currentCell = Database.getCode(Storage.getToken(), (int) currentCell.getId());

            userInput.setVisible(false);
            code.setVisible(true);
        }
        // After the edit operation is finished, the delete and copy buttons can be interacted with.
        delete.setDisable(false);
        copy.setDisable(false);
    }

    /**
     * Handles when the copy button is pressed
     */
    public void copyOnAction() {
        //Get the system clipboard so the code be copied accordingly
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putString(currentCell.getCode());
        clipboard.setContent(content);
    }

    /**
     * Handles when the delete button is pressed
     */
    public void deleteOnAction() {
        // handle the event when delete button is clicked
        currentListView.getItems().remove(currentCell);
        Database.removeCode(Storage.getToken(), (int) currentCell.getId());
    }
}
