package views.utilities.CodeViewUtilities;

import controllers.CodeViewControllers.CodeCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import code.Code;
import utilities.Utilities;

import java.io.IOException;


public class CodeCellFactory extends ListCell<Code> {

    private Parent graphic;

    ListView<Code> CodeListView;

    private final CodeCellController cellController;

    public CodeCellFactory(ListView<Code> parentListView) throws IOException {

        // Each cell only loads the FXML file once to speed up runtime
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("view/CodeViewFXML/CodeCell.fxml"));

        CodeListView = parentListView;

        try {
            graphic = loader.load();
        } catch (IOException e) {e.printStackTrace();}

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
