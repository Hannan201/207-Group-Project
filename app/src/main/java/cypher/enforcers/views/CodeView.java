package cypher.enforcers.views;

import cypher.enforcers.controllers.CodeViewControllers.CodeViewController;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.interfaces.Reversible;

/**
 * This class is responsible for displaying a view
 * which shows all the backup codes for a specific
 * social media account.
 */

public class CodeView extends View implements Reversible {

    private static final Logger logger = LoggerFactory.getLogger(CodeView.class);

    // An instance for this code-viewer view.
    private static View firstInstance = null;

    private CodeViewController controller = null;

    // The previous view for this code-viewer
    // view.
    private View previousView;

    /**
     * Create a new code-viewer view.
     */
    private CodeView() {
        initUI();
    }

    /**
     * Return the instance of this code-viewer view.
     *
     * @return Instance of this code-viewer view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new CodeView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this code-viewer
     * view.
     */
    @Override
    protected void initUI() {
        try {
            FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("view/CodeViewFXML/CodeView.fxml"));
            this.setRoot(loader.load());
            this.controller = loader.getController();
        } catch (Exception e) {
            logger.error("Failed to load FXML file: CodeView.fxml. Cause: ", e);
            e.printStackTrace();
        }

        this.names = new String[]{"CodeView.css",
                                  "CodeViewDM.css",
                                  "CodeViewHC.css"};

        this.loadStylesheets();
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

    public CodeViewController getCodeViewController() {
        return this.controller;
    }

}