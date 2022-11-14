package views;

/**
 * This class is responsible for displaying a home
 * page view for this application.
 */

public class HomePageView extends View {

    // An instance for this home page view.
    private View firstInstance = null;

    /**
     * Create a new home page view.
     */
    private HomePageView() {

    }

    /**
     * Return the instance of this home page view.
     *
     * @return Instance of this home page view.
     */
    @Override
    public View getInstance() {
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
}
