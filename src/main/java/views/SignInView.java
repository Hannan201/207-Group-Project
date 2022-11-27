package views;

import javafx.scene.Parent;

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

    }

    /**
     * Return the instance of this sign-in view.
     *
     * @return Instance of this sign-in view.
     */

    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SignInView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this sign-in
     * view.
     */
    @Override
    protected void initUI() {

    }

    /**
     * Switch this sign-in view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this sign-in view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this sign-in view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }

    @Override
    public Parent getRoot() {
        throw new UnsupportedOperationException();
    }
}
