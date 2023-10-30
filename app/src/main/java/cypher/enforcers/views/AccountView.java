package cypher.enforcers.views;

import cypher.enforcers.controllers.AccountViewController;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.interfaces.Reversible;

/**
 * This class is responsible for displaying a view
 * to show all the social media accounts for a specific
 * user of this application.
 */

public class AccountView extends View implements Reversible {

    // Logger for the account view.
    private static final Logger logger = LoggerFactory.getLogger(AccountView.class);

    // An instance for this account-viewer view.
    private static View firstInstance = null;

    private AccountViewController controller = null;

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
        try {
            FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/cypher/enforcers/view/AccountsView.fxml"));
            loader.setControllerFactory(View.CONTROLLER_FACTORY);
            this.setRoot(loader.load());
            this.controller = loader.getController();
        } catch (Exception e) {
            logger.error("Failed to load FXML file from resources: /cypher/enforcers/view/AccountsView.fxml. Cause: ", e);
            return;
        }

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
     * @return controller for the AccountView class.
     */
    public AccountViewController getAccountViewController() {
        return this.controller;
    }
}
