import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.SettingsView;
import views.View;

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
        // Entry point.
        stage.setScene(new Scene(SettingsView.getInstance().getRoot()));
        stage.show();
    }
}
