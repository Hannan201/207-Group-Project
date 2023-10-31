package cypher.enforcers.views.utilities.codeViewUtilities;

import cypher.enforcers.controllers.codeViewControllers.CodeCellController;
import cypher.enforcers.data.security.Code;
import cypher.enforcers.models.CodeModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import cypher.enforcers.utilities.Utilities;

import java.io.IOException;

/**
 * Class to represent a code cell.
 */
public class CodeCell extends ListCell<Code> {

    // Container for the cell data.
    private final Parent graphic;

    // The list view.
    private final ListView<Code> CodeListView;

    // Controller for the cell.
    private final CodeCellController cellController;

    // Mainly here to be passed to the controller.
    private CodeModel codeModel;

    /**
     * Set the code model.
     *
     * @param model The Code Model.
     */
    public void setCodeModel(CodeModel model) {
        this.codeModel = model;
    }


    public CodeCell(ListView<Code> parentListView) throws IOException {
        // Each cell only loads the FXML file once to speed up runtime
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/cypher/enforcers/view/CodeViewFXML/CodeCell.fxml"));

        CodeListView = parentListView;

        graphic = loader.load();

        cellController = loader.getController();
    }


    @Override
    protected void updateItem(Code code, boolean empty) {
        super.updateItem(code, empty);

        if (empty || code == null) {
            setText(null);
            setGraphic(null);
        } else{

            // pull data from the cell and apply it to the UI
            cellController.setCodeCell(code, CodeListView, codeModel);
            setGraphic(graphic);
        }
    }
}
