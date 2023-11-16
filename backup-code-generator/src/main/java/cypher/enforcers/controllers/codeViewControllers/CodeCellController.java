package cypher.enforcers.controllers.codeViewControllers;

import cypher.enforcers.data.security.dtos.Code;
import cypher.enforcers.models.CodeModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Controller for the code cell.
 */
public class CodeCellController {

    /** Logger for the code cell controller. */
    private static final Logger logger = LoggerFactory.getLogger(CodeCellController.class);

    /** Label that displays the code. */
    @FXML
    private Label code;

    /** Text field used to update the code. */
    @FXML
    private TextField userInput;

    /** Button that says Copy. */
    @FXML
    private Button copy;

    /** Button that says Delete. */
    @FXML
    private Button delete;

    /** Button that says Edit. */
    @FXML
    private Button edit;

    /** Reference to the parent list view. */
    private ListView<Code> currentListView;

    /** If this code is selected or not. */
    private boolean selected;

    /** Used to interact with the codes for a given account. */
    private final CodeModel codeModel;

    /**
     * Create the controller for a code cell with the required model.
     *
     * @param codeModel The model to interact with the codes.
     */
    public CodeCellController(CodeModel codeModel) {
        this.codeModel = codeModel;
    }

    /**
     * Stores the data of the current code, the code factory, and its corresponding listview.
     *
     * @param cell The current CodeCell
     * @param parent The ListView that contains this CodeCell
     */
    public void setCodeCell(Code cell, ListView<Code> parent) {
        currentListView = parent;
        selected = false;

        // Making the text of the code and text work in tandem with one another.
        code.setText(cell.code());
        userInput.setText(cell.code());

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
        // Handle the event when enter is pressed. This is done so that the user submit their edit more feasibly
        // Don't let the user enter an empty code.
        if (e.getCode() == KeyCode.ENTER && !userInput.getText().isEmpty()) {
            String logText = Objects.isNull(codeModel.getCurrentCode()) ?
                    "null" : codeModel.getCurrentCode().code();
            logger.debug("Switching code from {} to {}.", logText, userInput.getText());

            if (codeModel.updateCode(userInput.getText())) {
                code.setText((userInput.getText()));
                logger.info("Updated code from {} to {} successfully.", logText, userInput.getText());
            }

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

        String value = Objects.isNull(codeModel.getCurrentCode()) ?
                "" : codeModel.getCurrentCode().code();
        content.putString(value);
        clipboard.setContent(content);
    }

    /**
     * Handles when the delete button is pressed
     */
    public void deleteOnAction() {
        codeModel.deleteCode();
    }
}
