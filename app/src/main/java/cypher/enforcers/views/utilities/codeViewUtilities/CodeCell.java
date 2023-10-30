package cypher.enforcers.views.utilities.codeViewUtilities;

import cypher.enforcers.controllers.codeViewControllers.CodeCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import cypher.enforcers.code.CodeEntity;
import cypher.enforcers.utilities.Utilities;

import java.io.IOException;

/**
 * Class to represent a code cell.
 */
public class CodeCell extends ListCell<CodeEntity> {

    // Container for the cell data.
    private final Parent graphic;

    // The list view.
    private final ListView<CodeEntity> CodeListView;

    // Controller for the cell.
    private final CodeCellController cellController;

    public CodeCell(ListView<CodeEntity> parentListView) throws IOException {
        // Each cell only loads the FXML file once to speed up runtime
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/cypher/enforcers/view/CodeViewFXML/CodeCell.fxml"));

        CodeListView = parentListView;

        graphic = loader.load();

        cellController = loader.getController();
    }


    @Override
    protected void updateItem(CodeEntity code, boolean empty) {
        super.updateItem(code, empty);

        if (empty || code == null) {
            setText(null);
            setGraphic(null);
        } else{

            // pull data from the cell and apply it to the UI
            cellController.setCodeCell(code, CodeListView);
            setGraphic(graphic);
        }
    }
}
