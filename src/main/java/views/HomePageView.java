package views;

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
        this.cssFilesPaths = new String[]{"",  //Path to CSS file for light mode
                                          "",  //Path to CSS file for dark mode
                                          ""}; //Path to CSS file for high contrast mode
    }
}
