package views;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import user.Account;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

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

        Map<String, String> icons = Account.getIcons();
        URL url;

        url = Account.class.getClassLoader().getResource("images/icons8-discord-100.png");
        if (url != null) {
            String discord = url.toExternalForm();
            icons.put("discord", discord);
        }

        url = Account.class.getClassLoader().getResource("images/icons8-github-100.png");
        if (url != null) {
            String github = url.toExternalForm();
            icons.put("github", github);
        }

        url = Account.class.getClassLoader().getResource("images/icons8-google-100.png");
        if (url != null) {
            String google = url.toExternalForm();
            icons.put("google", google);
        }

        url = Account.class.getClassLoader().getResource("images/icons8-shopify-100.png");
        if (url != null) {
            String shopify = url.toExternalForm();
            icons.put("shopify", shopify);
        }

        url  = Account.class.getClassLoader().getResource("images/icons8-app-100.png");
        if (url != null) {
            String defaultIcon = url.toExternalForm();
            icons.put("default", defaultIcon);
        }
    }

    /**
     * Switch this view to dark mode.
     */
    public void switchToDarkMode() {
        this.currentThemePath = this.cssFilesPaths[1];

        Map<String, String> icons = Account.getIcons();
        URL url;

        url = Account.class.getClassLoader().getResource("images/discord_darkmode.png");
        if (url != null) {
            String discord = url.toExternalForm();
            icons.put("discord", discord);
        }

        url = Account.class.getClassLoader().getResource("images/github_darkmode.png");
        if (url != null) {
            String github = url.toExternalForm();
            icons.put("github", github);
        }

        url = Account.class.getClassLoader().getResource("images/google_darkmode.png");
        if (url != null) {
            String google = url.toExternalForm();
            icons.put("google", google);
        }

        url = Account.class.getClassLoader().getResource("images/shopify_darkmode.png");
        if (url != null) {
            String shopify = url.toExternalForm();
            icons.put("shopify", shopify);
        }

        url = Account.class.getClassLoader().getResource("images/app_darkmode.png");
        if (url != null) {
            String defaultIcon = url.toExternalForm();
            icons.put("default", defaultIcon);
        }
    }

    /**
     * Switch this view to high contrast mode.
     */
    public void switchToHighContrastMode() {
        this.currentThemePath = this.cssFilesPaths[2];

        Map<String, String> icons = Account.getIcons();
        URL url;

        url = Account.class.getClassLoader().getResource("images/hc-discord.png");
        if (url != null) {
            String discord = url.toExternalForm();
            icons.put("discord", discord);
        }

        url = Account.class.getClassLoader().getResource("images/hc-github.png");
        if (url != null) {
            String github = url.toExternalForm();
            icons.put("github", github);
        }

        url = Account.class.getClassLoader().getResource("images/hc-google.png");
        if (url != null) {
            String google = url.toExternalForm();
            icons.put("google", google);
        }

        url = Account.class.getClassLoader().getResource("images/hc-shopify.png");
        if (url != null) {
            String shopify = url.toExternalForm();
            icons.put("shopify", shopify);
        }

        url = Account.class.getClassLoader().getResource("images/hc-app.png");
        if (url != null) {
            String defaultIcon = url.toExternalForm();
            icons.put("default", defaultIcon);
        }
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
            URL url = this.getClass().getClassLoader().getResource("view/" + fileName);
            if (url != null) {
                this.root = FXMLLoader.load(url);
            }
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
     *              the array.
     * @param element The name of the file.
     */
    private void setIndex(int index, String element) {
        URL url = this.getClass().getClassLoader().getResource("css/" + element);
        this.cssFilesPaths[index] = url == null ? "" : url.toExternalForm();
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

    /**
     * Add a hover effect to a control to change the background
     * colour of a component by controlling the original colour,
     * colour on hover, and duration of the effect.
     *
     * @param control The UI component which the effect will be
     *                applied on.
     * @param fromFillColour The start colour of the component.
     * @param toFillColour The end colour of the component for
     *                     when the effect finishes.
     * @param duration The duration of the event.
     */
    public static void setHoverFillEffect(
            Control control,
            Paint fromFillColour,
            Paint toFillColour,
            double duration
    ) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(fromFillColour);

        FillTransition customTransition = new FillTransition();
        customTransition.setShape(rectangle);
        customTransition.setFromValue((Color) fromFillColour);
        customTransition.setToValue((Color) toFillColour);
        customTransition.setCycleCount(1);
        customTransition.setDuration(Duration.millis(duration));
        customTransition.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double v) {
                control.setBackground(
                        new Background(
                                new BackgroundFill(
                                        rectangle.getFill(),
                                        new CornerRadii(4),
                                        Insets.EMPTY
                                )
                        )
                );
                return v;
            }
        });

        customTransition.play();
    }
}
