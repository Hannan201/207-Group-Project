package views;

import javafx.scene.Parent;
import views.interfaces.Reversible;

/**
 * This class is responsible for displaying a view
 * to show all the social media accounts for a specific
 * user of this application.
 */

public class AccountView extends View implements Reversible {

    // An instance for this account-viewer view.
    private static View firstInstance = null;

    // The previous view for this account-viewer
    // view.
    private View previousView;

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

    /**
     * Get the previous view which was being
     * displayed before this account-viewer
     * view.
     *
     * @return The previous view for this
     * account-viewer view.
     */
    @Override
    public View getPreviousView() {
        return this.previousView;
    }

    /**
     * Set the previous view for this
     * account-viewer view to display.
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
