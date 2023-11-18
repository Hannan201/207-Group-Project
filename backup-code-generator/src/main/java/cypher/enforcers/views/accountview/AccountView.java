package cypher.enforcers.views.accountview;

import cypher.enforcers.controllers.AccountViewController;
import cypher.enforcers.views.View;
import javafx.fxml.FXMLLoader;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.interfaces.Reversible;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * to show all the social media accounts for a specific
 * user of this application.
 */
public class AccountView extends View implements Reversible {

    /** An instance for this account-viewer view. */
    private static View firstInstance = null;

    /** The controller for this account view. */
    private AccountViewController controller = null;

    /**
     * The previous view for this account-viewer
     * view.
     */
    private View previousView;

    /**
     * Create a new account-viewer view.
     *
     * @throws IOException If any errors occur when creating the accounts
     * view.
     * @throws NullPointerException If there's any missing data for
     * creating the accounts view.
     */
    private AccountView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this account-viewer view.
     *
     * @return Instance of this account-viewer view.
     * @throws IOException If any errors occur when trying to
     * retrieve the accounts view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the accounts view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new AccountView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this code-viewer
     * view.
     *
     * @throws IOException If any errors occur when loading in the
     * FXML file for the accounts view.
     * @throws NullPointerException If the FXML file or CSS files f
     * for the accounts view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("view/AccountsView.fxml"));
        loader.setControllerFactory(View.CONTROLLER_FACTORY);
        this.setRoot(loader.load());
        this.controller = loader.getController();

        this.loadStylesheets(
                "AccountsView.css",
                "AccountsViewDM.css",
                "AccountsViewHC.css"
        );
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

    /**
     *
     * Returns the controller for the AccountView class.
     *
     * @return Controller for the AccountView class.
     */
    public AccountViewController getAccountViewController() {
        return this.controller;
    }
}
