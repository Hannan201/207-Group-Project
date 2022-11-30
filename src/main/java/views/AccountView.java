package views;

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
        this.names = new String[]{"Name of CSS file for light mode.",
                                  "Name of CSS file for dark mode.",
                                  "Name of CSS file for high contrast mode."};

        this.loadStylesheets();

        this.loadRoot("Name of FXML file here");
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
