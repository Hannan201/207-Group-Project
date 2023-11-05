package cypher.enforcers.views;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * to sign up.
 */

public class SignUpView extends View {

    // An instance for this sign-up view.
    private static View firstInstance = null;

    /**
     * Create a new sign-up view.
     *
     * @throws IOException If any errors occur when creating the sign-up
     * view.
     * @throws NullPointerException If there's any missing data for
     * creating the sign-up view.
     */
    private SignUpView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this sign-up view.
     *
     * @return Instance of this sign-up view.
     * @throws IOException If any errors occur when trying to
     * retrieve the sign-up view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the sign-up view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new SignUpView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this sign-up
     * view.
     *
     * @throws IOException If any errors occur when loading in the
     * FXML file for the sign-up view.
     * @throws NullPointerException If the FXML file or CSS files for the
     * sign-up view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        this.loadRoot("SignUpView.fxml");

        this.loadStylesheets(
                "SignUpView.css",
                "SignUpViewDM.css",
                "SignUpViewHC.css"
        );
    }
}
