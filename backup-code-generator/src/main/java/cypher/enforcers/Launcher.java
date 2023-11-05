package cypher.enforcers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cypher.enforcers.views.*;

import java.io.IOException;

/**
 * This class is responsible for launching the backup
 * code manager application.
 */

public class Launcher extends Application {

    // Logger used for logging.
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    /**
     * Entry point for this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        logger.info("Launching application...");
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
        try {
            Utilities.prepare(stage);
            View view = HomePageView.getInstance();
            Scene scene = new Scene(view.getRoot());
            scene.getStylesheets().add(view.getCurrentThemePath());
            stage.setScene(scene);
            stage.show();
            logger.info("Displaying application window.");
        } catch (IOException e) {
            Utilities.onException(stage, e);
        }
    }
}