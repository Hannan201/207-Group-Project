package cypher.enforcers.views.utilities.codeViewUtilities;

import cypher.enforcers.controllers.CodeViewControllers.CodeCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import cypher.enforcers.code.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;

import java.io.IOException;


public class CodeCellFactory extends ListCell<Code> {

    private static final Logger logger = LoggerFactory.getLogger(CodeCellFactory.class);

    private Parent graphic;

    ListView<Code> CodeListView;

    private final CodeCellController cellController;

    public CodeCellFactory(ListView<Code> parentListView) throws IOException {

        // Each cell only loads the FXML file once to speed up runtime
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/view/CodeViewFXML/CodeCell.fxml"));

        CodeListView = parentListView;

        try {
            graphic = loader.load();
        } catch (IOException e) {
            logger.error("Failed to load FXML file: CodeCell.fxml. Cause: ", e);
            e.printStackTrace();
        }


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
            cellController.setCodeCell(code, CodeListView);
            setGraphic(graphic);
        }
    }
}
