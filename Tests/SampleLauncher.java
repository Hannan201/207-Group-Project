import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.parallel.Resources;
import views.HomePageView;
import views.SettingsView;
import views.View;

/**
 * This class is responsible for launching the backup
 * code manager application.
 */

public class SampleLauncher extends Application {

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

//        Parent root = FXMLLoader.load(getClass().getResource("view/HomePageView.fxml"));
//        Scene scene = new Scene(root);
//        stage.setTitle("Backup Code Generator");
//        String css = this.getClass().getResource("css/HomePageView.css").toExternalForm();
//        scene.getStylesheets().add(css);
//        stage.setScene(scene);
//        stage.show();

        Scene scene = new Scene(HomePageView.getInstance().getRoot());
        scene.getStylesheets().add(HomePageView.getInstance().getCurrentThemePath());
        stage.setScene(scene);
        stage.show();
    }
}
