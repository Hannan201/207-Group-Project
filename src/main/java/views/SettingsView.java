package views;

/**
 * This class is responsible for displaying a view
 * to change the settings for this application. Such
 * as the font and theme.
 */

public class SettingsView extends View {

    // An instance for this settings view.
    private View firstInstance = null;

    /**
     * Create a new settings view.
     */
    private SettingsView() {

    }

    /**
     * Return the instance of this settings view.
     *
     * @return Instance of this settings view.
     */
    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SettingsView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this settings
     * view.
     */
    @Override
    protected void initUI() {

    }

    /**
     * Switch this settings view to light mode.
     */
    @Override
    public void switchToLightMode() {

    }

    /**
     * Switch this settings view to dark mode.
     */
    @Override
    public void switchToDarkMode() {

    }

    /**
     * Switch this settings view to high contrast mode.
     */
    @Override
    public void switchToHighContrastMode() {

    }
}
