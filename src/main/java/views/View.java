package views;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Map;

/**
 * This class is responsible for displaying a specific
 * view in this application
 */

public abstract class View {

    // A reference to store the Scene
    // object being used to display
    // this view.
    private static Scene scene;

    // Parent root of this view, which is
    // the layout where all the components
    // are placed in.
    private Parent root;

    // Stores the path to the CSS (in
    // external form) which contains
    // the stylesheet for the current
    // theme of this application.
    private String currentThemePath;

    // Stores the paths to the CSS files
    // (in external form) in the following order:
    // Index 0: Path to the CSS file for light mode.
    // Index 1: Path to the CSS file for dark mode.
    // Index 2: Path to the CSS file for high contrast mode.
    protected String[] cssFilesPaths;

    /**
     * Initialise the UI elements for this view.
     */
    protected abstract void initUI();

    /**
     * Set the scene object being used
     * to display this view.
     *
     * @param newScene The scene object
     *                 being used to
     *                 display this view.
     */
    public static void setScene(Scene newScene) {
        scene = newScene;
    }

    /**
     * Get the scene being used to display
     * this view.
     *
     * @return The scene being used to display
     * this view.
     */
    public Scene getScene() {
        return scene;
    }

    public String getCurrentThemePath() {
        return this.currentThemePath;
    }

    /**
     * Switch this view to light mode.
     */
    public void switchToLightMode() {
        this.currentThemePath = this.cssFilesPaths[0];
    }

    /**
     * Switch this view to dark mode.
     */
    public void switchToDarkMode() {
        this.currentThemePath = this.cssFilesPaths[1];
    }

    /**
     * Switch this view to high contrast mode.
     */
    public void switchToHighContrastMode() {
        this.currentThemePath = this.cssFilesPaths[2];
    }

    /**
     * Return the parent root node of this view, which
     * contains all the element to be displayed.
     *
     * @return Root node of this view. Which is
     * the layout where all the components are
     * placed in.
     */
    public Parent getRoot() {
        return this.root;
    }

    /**
     * Set the parent root node of this
     * view, which contains all the elements
     * to be displayed,
     *
     * @param newRoot The new parent root
     *                which will display
     *                all the components
     *                for this view.
     */
    protected void setRoot(Parent newRoot) {
        this.root = newRoot;
    }
}
