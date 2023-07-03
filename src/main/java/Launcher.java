import controllers.utilities.Utilities;
import data.database.Database;
import data.Storage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ResourceUtilities;
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
            URI uri = ResourceUtilities.loadFileByURL("database/database.db").toURI();
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

        View view = Storage.getToken() == null
                ? HomePageView.getInstance() : AccountView.getInstance();
        Scene scene = new Scene(view.getRoot());
        adjustTheme();
        scene.getStylesheets().add(view.getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Adjust the theme of the application based on the user's setting
     * before they quit the application without logging out.
     */
    private static void adjustTheme() {
        if (Storage.getToken() != null) {
            Utilities.adjustTheme();
            Utilities.loadAccounts();
        }
    }

}
