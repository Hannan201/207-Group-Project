package views;

import javafx.scene.Parent;

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

    }

    /**
     * Switch this sign-up view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this sign-up view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this sign-up view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }

    /**
     * Return the parent root node of this sign-up
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
