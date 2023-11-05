package cypher.enforcers.views;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * to sign in.
 */

public class SignInView extends View {

    // An instance for this sign-in view.
    private static View firstInstance = null;

    /**
     * Create a new sign in view.
     *
     * @throws IOException If any errors occur when creating the sign-in
     * view.
     */
    private SignInView() throws IOException {
        initUI();
    }

    /**
     * Return the instance of this sign-in view.
     *
     * @return Instance of this sign-in view.
     * @throws IOException If any errors occur when trying to
     * retrieve the sign-in view.
     */
    public static View getInstance() throws IOException {
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
     */
    @Override
    protected void initUI() throws IOException {
        this.loadRoot("SignIn.fxml");

        this.loadStylesheets(
                "SignInView.css",
                "SignInViewDM.css",
                "SignInViewHC.css"
        );
    }
}
