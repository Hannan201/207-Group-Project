package cypher.enforcers.views;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * to sign in.
 */
public class SignInView extends View {

    /** An instance for this sign-in view. */
    private static View firstInstance = null;

    /**
     * Create a new sign in view.
     *
     * @throws IOException If any errors occur when creating the sign-in
     * view.
     * @throws NullPointerException If there's any missing data for
     * creating the sign-in view.
     */
    private SignInView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this sign-in view.
     *
     * @return Instance of this sign-in view.
     * @throws IOException If any errors occur when trying to
     * retrieve the sign-in view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the sign-in view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new SignInView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this sign-in
     * view.
     *
     * @throws IOException If any errors occur when loading in the
     * FXML file for the sign-in view.
     * @throws NullPointerException If the FXML file or CSS files for the
     * sign-in view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        this.loadRoot("SignIn.fxml");

        this.loadStylesheets(
                "SignInView.css",
                "SignInViewDM.css",
                "SignInViewHC.css"
        );
    }
}
