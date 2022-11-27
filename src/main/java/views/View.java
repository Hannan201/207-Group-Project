package views;

import javafx.scene.Parent;

/**
 * This class is responsible for displaying a specific
 * view in this application
 */

public abstract class View {

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

    /**
     * Return the parent root node of this view, which
     * contains all the element to be displayed.
     *
     * @return Root node of this view. Which is
     * the layout where all the components are
     * placed in.
     */
    public abstract Parent getRoot();
}
