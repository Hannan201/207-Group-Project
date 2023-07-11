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

    /**
     * Entry point for this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        try {
            URI uri = Utilities.loadFileByURL("database/database.db").toURI();
            String path = Paths.get(uri).toString();
            Database.setConnectionSource(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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

        View view = initialize();
        Scene scene = new Scene(view.getRoot());
        scene.getStylesheets().add(view.getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initialize user settings and data if the user closed the
     * application without logging out to load the correct view.
     */
    private static View initialize() {
        if (Storage.getToken() != null) {
            Utilities.adjustTheme();
            Utilities.loadAccounts();
            return AccountView.getInstance();
        }

        return HomePageView.getInstance();
    }

}