package views;

import javafx.scene.Parent;

/**
 * This class is responsible for displaying a home
 * page view for this application.
 */

public class HomePageView extends View {

    // An instance for this home page view.
    private static View firstInstance = null;

    /**
     * Create a new home page view.
     */
    private HomePageView() {
        initUI();
    }

    /**
     * Return the instance of this home page view.
     *
     * @return Instance of this home page view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new HomePageView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this home page
     * view.
     */
    @Override
    protected void initUI() {

    }

    /**
     * Switch this home page view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this home page view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this home page view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }

    /**
     * Return the parent root node of this home
     * page view, which contains all the element
     * to be displayed.
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
