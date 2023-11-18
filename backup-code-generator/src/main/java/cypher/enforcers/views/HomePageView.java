package cypher.enforcers.views;

import java.io.IOException;

/**
 * This class is responsible for displaying a home
 * page view for this application.
 */

public class HomePageView extends View {

    /** An instance for this home page view. */
    private static View firstInstance = null;

    /**
     * Create a new home page view.
     *
     * @throws IOException If any errors occur when creating the home page
     * view.
     * @throws NullPointerException If there's any missing data for
     * creating the home page view.
     */
    private HomePageView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this home page view.
     *
     * @return Instance of this home page view.
     * @throws IOException If any errors occur when trying to
     * retrieve the home page view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the home page view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new HomePageView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this home page
     * view.
     *
     * @throws IOException If any errors occur when loading in the
     * FXML file for the home page view.
     * @throws NullPointerException If the FXML file or CSS files for the
     * home page view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        this.loadRoot("HomePageView.fxml");

        this.loadStylesheets(
                "HomePageView.css",
                "HomePageViewDM.css",
                "HomePageViewHC.css"
        );
    }
}
