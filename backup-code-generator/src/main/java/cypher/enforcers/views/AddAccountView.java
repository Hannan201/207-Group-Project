package cypher.enforcers.views;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * to add a new social media account.
 */
public class AddAccountView extends View {

    /** An instance for this add-new-account view. */
    private static View firstInstance = null;

    /**
     * Create a new add-new-account view.
     *
     * @throws IOException If any errors occur when creating the
     * create-account view.
     * @throws NullPointerException If there's any missing data for
     * creating the create-account view.
     */
    private AddAccountView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this create-account view.
     *
     * @return Instance of this add-new-account view.
     * @throws IOException If any errors occur when trying to
     * retrieve the create-account view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the create-account view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new AddAccountView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this create-account
     * view.
     *
     * @throws IOException If any errors occur when loading in the
     * FXML file for the create-account view.
     * @throws NullPointerException If the FXML file or CSS files for the
     * create-account view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        this.loadRoot("CreateAccountView.fxml");

        this.loadStylesheets(
                "CreateAccountView.css",
                "CreateAccountViewDM.css",
                "CreateAccountViewHC.css"
        );
    }
}
