package cypher.enforcers;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.data.database.Database;
import cypher.enforcers.data.Storage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cypher.enforcers.views.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class is responsible for launching the backup
 * code manager application.
 */

public class Launcher extends Application {

    // Logger used for logging.
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    // Path to the database file (relative to the resources folder).
    private static final String PATH_TO_DATABASE = "/cypher/enforcers/database/database.db";

    // Path to the logback configuration file (relative o the resources folder).
    private static final String PATH_TO_LOGBACK_CONFIG = "/cypher/enforcers/logback.xml";

    /**
     * Entry point for this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        configureLogger();
        logger.info("Starting application...");

        Database.setConnectionSource(PATH_TO_DATABASE);

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
    private View initialize() {
        if (Storage.getToken() != null) {
            logger.debug("Logged in user found, retrieving data.");
            Utilities.adjustTheme();
            Utilities.loadAccounts();
            return AccountView.getInstance();
        }

        logger.debug("No logged in user found, displaying home page view.");
        return HomePageView.getInstance();
    }

    /**
     * Configure logger to use configuration file.
     */
    private static void configureLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        InputStream configStream = Utilities.loadFileByInputStream(PATH_TO_LOGBACK_CONFIG);
        configurator.setContext(loggerContext);
        try {
            configurator.doConfigure(configStream); // loads logback file
        } catch (JoranException e) {
            logger.warn("Failed to configure logback file. Cause: ", e);
            e.printStackTrace();
        }
        try {
            configStream.close();
        } catch (IOException closeException) {
            logger.warn("Failed to close stream for logback file. Cause: ", closeException);
            closeException.printStackTrace();
        }
    }

}