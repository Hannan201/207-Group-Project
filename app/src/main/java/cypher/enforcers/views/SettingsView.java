package cypher.enforcers.views;

import cypher.enforcers.controllers.SettingsViewController;
import cypher.enforcers.views.interfaces.Reversible;

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
     */
    private SettingsView() {
        initUI();
    }

    /**
     * Return the instance of this settings view.
     *
     * @return Instance of this settings view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SettingsView();
            SettingsViewController.addView(firstInstance);
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this settings
     * view.
     */
    @Override
    protected void initUI() {
        this.names = new String[]{"SettingsView.css",
                                  "SettingsViewDM.css",
                                  "SettingsViewHC.css"};

        this.loadStylesheets();

        this.loadRoot("SettingsView.fxml");
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
