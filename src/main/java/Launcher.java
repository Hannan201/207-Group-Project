import data.Database;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.HomePageView;

/**
 * This class is responsible for launching the backup
 * code manager application.
 */

public class Launcher extends Application {

    private static final String PATH_TO_USERS_FILE = "src/main/java/data/users.ser";
    private static final String PATH_TO_CONFIG_FILE = "src/main/java/data/config.ser";

    /**
     * Entry point for this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        Database.setConfigurationsSource(PATH_TO_CONFIG_FILE);
        Database.setUsersSource(PATH_TO_USERS_FILE);
        launch(args);
    }

    /**
     * The main entry point for this JavaFX application.
     *
     * @param stage Primary stage for this application for
     *              which the scene can be set.
     * @throws Exception In case something goes wrong.
     */
    @Override
    public void start(Stage stage) throws Exception {

        stage.setOnCloseRequest(windowEvent -> {
            Database.logUserOut();
        });

        Parent root = HomePageView.getInstance().getRoot();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(HomePageView.getInstance().getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
    }
}
