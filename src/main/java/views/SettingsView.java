package views;

import javafx.scene.Parent;

/**
 * This class is responsible for displaying a view
 * to change the settings for this application. Such
 * as the font and theme.
 */

public class SettingsView extends View {

    // An instance for this settings view.
    private static View firstInstance = null;

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

    /**
     * Return the parent root node of this settings
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
