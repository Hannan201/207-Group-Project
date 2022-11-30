package views;

/**
 * This class is responsible for displaying a view
 * to sign up.
 */

public class SignUpView extends View {

    // An instance for this sign-up view.
    private static View firstInstance = null;

    /**
     * Create a new sign-up view.
     */
    private SignUpView() {
        initUI();
    }

    /**
     * Return the instance of this sign-up view.
     *
     * @return Instance of this sign-up view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SignUpView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this sign-up
     * view.
     */
    @Override
    protected void initUI() {
        this.cssFilesPaths[0] = ""; //Path to CSS file for light mode.
        this.cssFilesPaths[1] = ""; //Path to CSS file for dark mode.
        this.cssFilesPaths[2] = ""; //Path to CSS file for high contrast mode.
    }
}
