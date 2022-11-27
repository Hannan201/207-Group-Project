package views;

import javafx.scene.Parent;

/**
 * This class is responsible for displaying a view
 * to show all the social media accounts for a specific
 * user of this application.
 */

public class AccountView extends View {

    // An instance for this account-viewer view.
    private static View firstInstance = null;

    /**
     * Create a new account-viewer view.
     */
    private AccountView() {
        initUI();
    }

    /**
     * Return the instance of this account-viewer view.
     *
     * @return Instance of this account-viewer view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new AccountView();
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
     * Switch this account-viewer view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this account-viewer view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this account-viewer view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }

    /**
     * Return the parent root node of this
     * account-viewer view, which contains all
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
