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

    /**
     * Return the parent root node of this sign-in
     * view, which contains all the element to
     * be displayed.
     *
     * @return Root node of this view. Which is
     * the layout where all the components are
     * placed in.
     */
    @Override
    public Parent getRoot() {
        throw new UnsupportedOperationException();
    }
}
