package cypher.enforcers.views.codeview;

import cypher.enforcers.controllers.codeViewControllers.CodeViewController;
import cypher.enforcers.views.View;
import javafx.fxml.FXMLLoader;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.interfaces.Reversible;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * which shows all the backup codes for a specific
 * social media account.
 */
public class CodeView extends View implements Reversible {

    /** An instance for this code-viewer view. */
    private static View firstInstance = null;

    /** The controller for this code view. */
    private CodeViewController controller = null;

    // The previous view for this code-viewer
    // view.
    private View previousView;

    /**
     * Create a new code-viewer view.
     *
     * @throws IOException If any errors occur when creating the code
     * view.
     * @throws NullPointerException If there's any missing data for
     * creating the code view.
     */
    private CodeView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this code-viewer view.
     *
     * @return Instance of this code-viewer view.
     * @throws IOException If any errors occur when trying to
     * retrieve the code view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the code view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new CodeView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this code-viewer
     * view.
     *
     * @throws IOException If any errors occur when loading in the
     * FXML file for the code view.
     * @throws NullPointerException If the FXML file or CSS files for the
     * code view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("view/CodeViewFXML/CodeView.fxml"));
        loader.setControllerFactory(View.CONTROLLER_FACTORY);
        this.setRoot(loader.load());
        this.controller = loader.getController();

        this.loadStylesheets(
                "CodeView.css",
                "CodeViewDM.css",
                "CodeViewHC.css"
        );
    }

    /**
     * Get the previous view which was being
     * displayed before this code-viewer
     * view.
     *
     * @return The previous view for this
     * code-viewer view.
     */
    @Override
    public View getPreviousView() {
        return this.previousView;
    }

    /**
     * Set the previous view for this
     * code-viewer to display.
     *
     * @param newPreviousView The new previous
     *                        view for this
     *                        application.
     */
    @Override
    public void setPreviousView(View newPreviousView) {
        this.previousView = newPreviousView;
    }

    /**
     * Returns the controller for the CodeView class.
     *
     * @return Controller for the CodeView class.
     */
    public CodeViewController getCodeViewController() {
        return this.controller;
    }

}
