package views;

import controllers.SignInController;

/**
 * This class is responsible for displaying a view
 * to sign in.
 */

public class SignInView extends View {

    // An instance for this sign-in view.
    private static View firstInstance = null;

    /**
     * Create a new sign in view.
     */
    private SignInView() {
        initUI();
    }

    /**
     * Return the instance of this sign-in view.
     *
     * @return Instance of this sign-in view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SignInView();
            SignInController.addView(firstInstance);
            SignInController.addView(SettingsView.getInstance());
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this sign-in
     * view.
     */
    @Override
    protected void initUI() {
        this.names = new String[]{"SignInView.css",
                                  "SignInViewDM.css",
                                  "SignInViewHC.css"};

        this.loadStylesheets();

        this.loadRoot("SignIn.fxml");
    }
}
