package views.utilities.CodeViewUtilities;

import controllers.CodeViewControllers.CodeCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;



import java.io.IOException;


public class CodeCellFactory extends ListCell<CodeCell> {

    private FXMLLoader loader;

    private Parent graphic;

    ListView<CodeCell> TestListView;

    private final CodeCellController cellController;

    public CodeCellFactory(ListView<CodeCell> parentListView) throws IOException {

        // Each cell only laods the FXML file once to speed up runtime
        loader = new FXMLLoader(getClass().getClassLoader().getResource("view/CodeViewFXML/CodeCell.fxml"));

        TestListView = parentListView;

        try {
            graphic = loader.load();
        } catch (IOException e) {e.printStackTrace();}

        cellController = loader.getController();

    }


    @Override
    protected void updateItem(CodeCell codecell, boolean empty) {
        super.updateItem(codecell, empty);

        if (empty || codecell == null) {
            setText(null);
            setGraphic(null);
        } else{

            // pull data from the cell and apply it to the UI
            cellController.setCodeCell(codecell, this, TestListView);
            setGraphic(graphic);
        }
    }
}
