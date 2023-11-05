package cypher.enforcers.views;

import cypher.enforcers.controllers.SettingsViewController;
import cypher.enforcers.views.interfaces.Reversible;

import java.io.IOException;

/**
 * This class is responsible for displaying a view
 * to change the settings for this application. Such
 * as the font and theme.
 */

public class SettingsView extends View implements Reversible {

    // An instance for this settings view.
    private static View firstInstance = null;

    // The previous view for this settings view.
    private View previousView;

    /**
     * Create a new settings view.
     *
     * @throws IOException If any errors occur when creating the settings
     * view.
     * @throws NullPointerException If there's any missing data for
     * creating the settings view.
     */
    private SettingsView() throws IOException, NullPointerException {
        initUI();
    }

    /**
     * Return the instance of this settings view.
     *
     * @return Instance of this settings view.
     * @throws IOException If any errors occur when trying to
     * retrieve the settings view.
     * @throws NullPointerException If there's any missing data when
     * trying to retrieve the settings view.
     */
    public static View getInstance() throws IOException, NullPointerException {
        if (firstInstance == null) {
            firstInstance = new SettingsView();
            SettingsViewController.addView(firstInstance);
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this settings
     * view.
     * @throws IOException If any errors occur when loading in the
     * FXML file for the settings view.
     * @throws NullPointerException If the FXML file or CSS files for the
     * settings view cannot be found from resources.
     */
    @Override
    protected void initUI() throws IOException, NullPointerException {
        this.loadRoot("SettingsView.fxml");

        this.loadStylesheets(
                "SettingsView.css",
                "SettingsViewDM.css",
                "SettingsViewHC.css"
        );
    }

    /**
     * Get the previous view which was being
     * displayed before this settings-view.
     *
     * @return The previous view for this
     * settings view.
     */
    @Override
    public View getPreviousView() {
        return this.previousView;
    }

    /**
     * Set the previous view for this
     * settings-view to display.
     *
     * @param newPreviousView The new previous
     *                        view for this
     *                        application.
     */
    @Override
    public void setPreviousView(View newPreviousView) {
        this.previousView = newPreviousView;
    }
}
