package cypher.enforcers.views;

import cypher.enforcers.controllers.*;
import cypher.enforcers.controllers.codeViewControllers.CodeViewController;
import cypher.enforcers.data.implementations.SQLiteHelper;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.models.AccountModel;
import cypher.enforcers.models.CodeModel;
import cypher.enforcers.models.UserModel;
import cypher.enforcers.views.themes.Theme;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import cypher.enforcers.data.entities.AccountEntity;

import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is responsible for displaying a specific
 * view in this application
 */

public abstract class View {

    // Logger for the view class.
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
    protected String[] cssFilesPaths = new String[Theme.values().length];

    public static final Callback<Class<?>, Object> CONTROLLER_FACTORY = new Callback<>() {
        private static final DatabaseService dbService = new SQLiteHelper();

        static {
            dbService.connect();
        }

        private final UserModel userModel = Utilities.prepareUserModel(dbService);

        private final AccountModel accountModel = Utilities.prepareAccountModel(dbService);

        private final CodeModel codeModel = Utilities.prepareCodeModel(dbService);

        @Override
        public Object call(Class<?> param) {
            Object o;
            try {
                o = param.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (param == SignInController.class) {
                ((SignInController) o).setUserModel(userModel);
            } else if (param == SignUpController.class) {
                ((SignUpController) o).setUserModel(userModel);
            } else if (param == HomePageController.class) {
                ((HomePageController) o).setUserModel(userModel);
            } else if (param == AccountViewController.class) {
                ((AccountViewController) o).setUserModel(userModel);
                ((AccountViewController) o).setAccountModel(accountModel);
            } else if (param == CreateAccountController.class) {
                ((CreateAccountController) o).setUserModel(userModel);
                ((CreateAccountController) o).setAccountModel(accountModel);
            } else if (param == CodeViewController.class) {
                ((CodeViewController) o).setUserModel(userModel);
                ((CodeViewController) o).setAccountModel(accountModel);
                ((CodeViewController) o).setCodeModel(codeModel);
            } else if (param == SettingsViewController.class) {
                ((SettingsViewController) o).setUserModel(userModel);
                ((SettingsViewController) o).setAccountModel(accountModel);
            }

            return o;
        }
    };

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
                AccountEntity.getIcons(),
                "/cypher/enforcers/images/icons8-discord-100.png",
                "/cypher/enforcers/images/icons8-github-100.png",
                "/cypher/enforcers/images/icons8-google-100.png",
                "/cypher/enforcers/images/icons8-shopify-100.png",
                "/cypher/enforcers/images/icons8-app-100.png"
        );
    }

    /**
     * Switch this view to dark mode.
     */
    public void switchToDarkMode() {
        this.currentThemePath = this.cssFilesPaths[1];

        Utilities.updateIcons(
                AccountEntity.getIcons(),
                "/cypher/enforcers/images/discord_darkmode.png",
                "/cypher/enforcers/images/github_darkmode.png",
                "/cypher/enforcers/images/google_darkmode.png",
                "/cypher/enforcers/images/shopify_darkmode.png",
                "/cypher/enforcers/images/app_darkmode.png"
        );
    }

    /**
     * Switch this view to high contrast mode.
     */
    public void switchToHighContrastMode() {
        this.currentThemePath = this.cssFilesPaths[2];

        Utilities.updateIcons(
                AccountEntity.getIcons(),
                "/cypher/enforcers/images/hc-discord.png",
                "/cypher/enforcers/images/hc-github.png",
                "/cypher/enforcers/images/hc-google.png",
                "/cypher/enforcers/images/hc-shopify.png",
                "/cypher/enforcers/images/hc-app.png"
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
            FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/cypher/enforcers/view/" + fileName));
            loader.setControllerFactory(CONTROLLER_FACTORY);
            this.root = loader.load();
        } catch (IOException e) {
            logger.error(String.format("Failed to load FXML file: %s. Cause: ", fileName), e);
        }
    }

    /**
     * Load the stylesheets (CSS files) for this
     * view.
     */
    protected void loadStylesheets(String ... files) {
        for (int i = 0; i < files.length; i++) {
            this.cssFilesPaths[i] = Utilities.loadFileByURL(
                    "/cypher/enforcers/css/" + files[i]
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
     * A utility method to close a window that belongs
     * to a given node.
     *
     * @param node the node to close the scene for.
     */
    public static void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
