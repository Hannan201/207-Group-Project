package cypher.enforcers.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import cypher.enforcers.models.Account;

import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;

import java.io.IOException;

/**
 * This class is responsible for displaying a specific
 * view in this application
 */

public abstract class View {

    private static final Logger logger = LoggerFactory.getLogger(View.class);

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

        Utilities.updateIcons(
                Account.getIcons(),
                "images/icons8-discord-100.png",
                "images/icons8-github-100.png",
                "images/icons8-google-100.png",
                "images/icons8-shopify-100.png",
                "images/icons8-app-100.png"
        );
    }

    /**
     * Switch this view to dark mode.
     */
    public void switchToDarkMode() {
        this.currentThemePath = this.cssFilesPaths[1];

        Utilities.updateIcons(
                Account.getIcons(),
                "images/discord_darkmode.png",
                "images/github_darkmode.png",
                "images/google_darkmode.png",
                "images/shopify_darkmode.png",
                "images/app_darkmode.png"
        );
    }

    /**
     * Switch this view to high contrast mode.
     */
    public void switchToHighContrastMode() {
        this.currentThemePath = this.cssFilesPaths[2];

        Utilities.updateIcons(
                Account.getIcons(),
                "images/hc-discord.png",
                "images/hc-github.png",
                "images/hc-google.png",
                "images/hc-shopify.png",
                "images/hc-app.png"
        );
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
            this.root = FXMLLoader.load(
                    Utilities.loadFileByURL("/view/" + fileName)
            );
        } catch (IOException e) {
            logger.error(String.format("Failed to load FXML file: %s. Cause: ", fileName), e);
            e.printStackTrace();
        }
    }

    /**
     * Load the stylesheets (CSS files) for this
     * view.
     */
    protected void loadStylesheets() {
        for (int i = 0; i < this.cssFilesPaths.length; i++) {
            this.cssFilesPaths[i] = Utilities.loadFileByURL(
                    "/css/" + this.names[i]
                    ).toExternalForm();
        }

        // Set the default theme to light mode.
        this.currentThemePath = this.cssFilesPaths[0];
    }

    /**
     * A utility method to switch
     * the scene for one view to another
     * view.
     *
     * @param source The source view, which the
     *               scene is currently set to.
     * @param destination The new view, for
     *                    which the scene should
     *                    be set to.
     */
    public static void switchSceneTo(View source, View destination) {
        Scene scene = source.getRoot().getScene();
        scene.getStylesheets().clear();

        scene.setRoot(destination.getRoot());
        scene.getStylesheets().add(destination.getCurrentThemePath());
    }


    /**
     * A utility method to make a new window
     * appear to display contents for a specific
     * view.
     *
     * @param view view containing the contents
     *             which are to be displayed.
     */
    public static void loadNewWindow(View view) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        if (view.getRoot().getScene() == null) {
            Scene scene = new Scene(view.getRoot());
            scene.getStylesheets().add(view.getCurrentThemePath());
            stage.setScene(scene);
        } else {
            Scene scene = view.getRoot().getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(view.getCurrentThemePath());
            stage.setScene(view.getRoot().getScene());
        }
        stage.show();
    }

    /**
     * A utility method to close a window based
     * the ActionEvent that occurs.
     *
     * @param e The ActionEvent that occurred.
     */
    public static void closeWindow(ActionEvent e) {
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * A utility method to close a window based
     * the KeyEvent that occurs.
     *
     * @param e The KeyEvent that occurred.
     */
    public static void closeWindow(KeyEvent e) {
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
