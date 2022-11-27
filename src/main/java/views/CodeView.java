package views;

import javafx.scene.Parent;

/**
 * This class is responsible for displaying a view
 * which shows all the backup codes for a specific
 * social media account.
 */

public class CodeView extends View {

    // An instance for this code-viewer view.
    private static View firstInstance = null;

    /**
     * Create a new code-viewer view.
     */
    private CodeView() {

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

    }

    /**
     * Switch this code-viewer view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this code-viewer view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this code-viewer view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }

    /**
     * Return the parent root node of this
     * code-viewer view, which contains all
     * the element to be displayed.
     *
     * @return Root node of this view. Which is
     * the layout where all the components are
     * placed in.
     */
    @Override
    public Parent getRoot() {
        throw new UnsupportedOperationException();
    }
}
