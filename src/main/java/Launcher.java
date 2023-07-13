import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Utilities;
import data.database.Database;
import data.Storage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * This class is responsible for launching the backup
 * code manager application.
 */

public class Launcher extends Application {

    // Logger used for logging.
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    // Path to the database file (relative to the resources folder).
    private static final String PATH_TO_DATABASE = "database/database.db";

    /**
     * Entry point for this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        logger.info("Starting application...");
        try {
            URI uri = Utilities.loadFileByURL(PATH_TO_DATABASE).toURI();
            String path = Paths.get(uri).toString();
            Database.setConnectionSource(path);
        } catch (URISyntaxException e) {
            logger.error("Connection to database failed. Application will function, but no new changes will be saved.", e);
            e.printStackTrace();
        }

        logger.debug("Launching JavaFX application...");
        launch(args);
    }

    /**
     * The main entry point for this JavaFX application.
     *
     * @param stage Primary stage for this application for
     *              which the scene can be set.
     */
    @Override
    public void start(Stage stage) {
        ((AccountView) AccountView.getInstance()).getAccountViewController()
                .configureStage(stage);
        logger.debug("Configured action for stage when exited.");

        View view = initialize();
        Scene scene = new Scene(view.getRoot());
        scene.getStylesheets().add(view.getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
        logger.info("Displaying application window.");
    }

    /**
     * Initialize user settings and data if the user closed the
     * application without logging out to load the correct view.
     */
    private static View initialize() {
        if (Storage.getToken() != null) {
            logger.debug("Logged in user found, retrieving data.");
            Utilities.adjustTheme();
            Utilities.loadAccounts();
            return AccountView.getInstance();
        }

        logger.debug("No logged in user found, displaying home page view.");
        return HomePageView.getInstance();
    }

}