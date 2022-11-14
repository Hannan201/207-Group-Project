package views;

/**
 * This class is responsible for displaying a specific
 * view in this application
 */

public abstract class View {

    /**
     * Return the instance of this View.
     *
     * @return Instance of this view.
     */
    public abstract View getInstance();

    /**
     * Initialise the UI elements for this view.
     */
    protected abstract void initUI();

    /**
     * Switch this view to light mode.
     */
    public abstract void switchToLightMode();

    /**
     * Switch this view to dark mode.
     */
    public abstract void switchToDarkMode();

    /**
     * Switch this view to high contrast mode.
     */
    public abstract void switchToHighContrastMode();
}
