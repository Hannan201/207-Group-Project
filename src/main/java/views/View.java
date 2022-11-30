package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * This class is responsible for displaying a specific
 * view in this application
 */

public abstract class View {

    // Parent root of this view, which is
    // the layout where all the components
    // are placed in.
    private Parent root;

    // Stores the path to the CSS (in
    // external form) which contains
    // the stylesheet for the current
    // theme of this application.
    protected String currentThemePath;

    // Stores the paths to the CSS files
    // (in external form) in the following order:
    // Index 0: Path to the CSS file for light mode.
    // Index 1: Path to the CSS file for dark mode.
    // Index 2: Path to the CSS file for high contrast mode.
    protected String[] cssFilesPaths = new String[3];

    // Stores the names of the CSS files in the
    // Following order:
    // Index 0: Name of the CSS file for light mode.
    // Index 1: Name of the CSS file for dark mode.
    // Index 2: Name of the CSS file for high contrast mode.
    protected String[] names;

    /**
     * Initialise the UI elements for this view.
     */
    protected abstract void initUI();

    /**
     * Get the path to the CSS file (in
     * external form) which is being used
     * to style the current view.
     *
     * @return Path to the CSS file (in
     * external form) that is currently in use.
     */
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

    /**
     * Load the FXML file for this view and
     * store it.
     *
     * @param fileName The name of the FXML file.
     */
    protected void loadRoot(String fileName) {
        try {
            this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the stylesheets (CSS files) for this
     * view.
     */
    protected void loadStylesheets() {
        for (int i = 0; i < this.cssFilesPaths.length; i++) {
            this.setIndex(i, this.names[i]);
        }

        // Set the default theme to light mode.
        this.currentThemePath = this.cssFilesPaths[0];
    }

    /**
     * Store the path of a file (in
     * external form) in the resources/view
     * folder in an array to store file paths
     * at a specific index. if the file exists
     * inside the resources/view folder,
     * the path will be stored, otherwise
     * an empty string. The resources folder
     * is where the resources for this
     * application are stored. This
     * function also assumes there's
     * already a view folder made
     * inside the resource folder.
     *
     * @param index The index at which to
     *              store the path inside
     *              the names array.
     * @param element The name of the file.
     */
    private void setIndex(int index, String element) {
        URL url = this.getClass().getClassLoader().getResource("view/" + element);
        this.cssFilesPaths[index] = url == null ? "" : url.toExternalForm();
    }
}
