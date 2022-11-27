package views;

import javafx.scene.Parent;

/**
 * This class is responsible for displaying a view
 * to add a new social media account.
 */

public class AddAccountView extends View {

    // An instance for this add-new-account view.
    private static View firstInstance = null;

    /**
     * Create a new add-new-account view.
     */
    private AddAccountView() {
        initUI();
    }

    /**
     * Return the instance of this add-new-account view.
     *
     * @return Instance of this add-new-account view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new AddAccountView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this add-new-account
     * view.
     */
    @Override
    protected void initUI() {

    }

    /**
     * Switch this add-new-code view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this add-new-code view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this add-new-code view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }

    /**
     * Return the parent root node of this
     * add-new-account view, which contains
     * all the element to be displayed.
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
