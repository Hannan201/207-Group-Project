package cypher.enforcers;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
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

    // Path to the logback configuration file (relative to the resources folder).
    private static final String PATH_TO_LOGBACK_CONFIG = "/cypher/enforcers/logback.xml";

    /**
     * Entry point for this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        logger.trace("Configuring logger....");
        configureLogger();

        logger.info("Starting application...");

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
        logger.debug("Configured action for stage when exited.");

        View view = HomePageView.getInstance();
        Scene scene = new Scene(view.getRoot());
        scene.getStylesheets().add(view.getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
        logger.info("Displaying application window.");
    }

    /**
     * Configure logger to use configuration file.
     */
    private static void configureLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        try (InputStream configStream = Utilities.loadFileByInputStream(PATH_TO_LOGBACK_CONFIG)) {
            configurator.doConfigure(configStream); // loads logback file
        } catch (JoranException e) {
            logger.error(String.format("Failed to configure logback file: %s. Cause: ", PATH_TO_LOGBACK_CONFIG), e);
        } catch (IOException ioException) {
            logger.error(String.format("Failed to load logback file: %s. Cause: ", PATH_TO_LOGBACK_CONFIG), ioException);
        }
    }

}