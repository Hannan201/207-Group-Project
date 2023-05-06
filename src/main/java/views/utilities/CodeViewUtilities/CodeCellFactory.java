package views.utilities.CodeViewUtilities;

import controllers.CodeViewControllers.CodeCellController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.io.IOException;


public class CodeCellFactory extends ListCell<CodeCell> {

    private Parent graphic;

    ListView<CodeCell> CodeListView;

    private final CodeCellController cellController;

    private DoubleProperty baseSize;

    private ObjectProperty<Font> baseFontSize;

    private ObjectProperty<Font> baseLabelSize;

    public void setDefaultSizes(
            DoubleProperty newBaseSize,
            ObjectProperty<Font> newBaseFontSize,
            ObjectProperty<Font> newBaseLabelSize
            ) {
        this.baseSize = newBaseSize;
        this.baseFontSize = newBaseFontSize;
        this.baseLabelSize = newBaseLabelSize;
    }

    public CodeCellFactory(ListView<CodeCell> parentListView) throws IOException {

        // Each cell only loads the FXML file once to speed up runtime
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/CodeViewFXML/CodeCell.fxml"));

        CodeListView = parentListView;

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
            cellController.setCodeCell(codecell, CodeListView);
            cellController.setBindings(
                    baseSize,
                    baseFontSize,
                    baseLabelSize
            );
            setGraphic(graphic);
        }
    }
}
