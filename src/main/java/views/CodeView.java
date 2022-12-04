package views;

import views.interfaces.Reversible;

/**
 * This class is responsible for displaying a view
 * which shows all the backup codes for a specific
 * social media account.
 */

public class CodeView extends View implements Reversible {

    // An instance for this code-viewer view.
    private static View firstInstance = null;

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
        this.names = new String[]{"Name of CSS file for light mode.",
                                  "Name of CSS file for dark mode.",
                                  "Name of CSS file for high contrast mode."};

        this.loadStylesheets();

        this.loadRoot("src/main/resources/view/CodeViewFXML/CodeView.fxml");
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
}
