import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.managers.ThemeSwitcher;
import data.Database;
import data.Storage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ResourceUtilities;
import views.*;

import java.util.ArrayList;
import java.util.List;

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
        String path = ResourceUtilities.loadFileByURL("database/database.db").getPath();
        Database.setConnectionSource(path);
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
            String preferredTheme = Database.getTheme(Storage.getToken());
            if (preferredTheme != null) {

                // Switch to the respective theme.
                if (!preferredTheme.equals("light mode")) {
                    List<View> views = new ArrayList<>(
                            List.of(
                                    HomePageView.getInstance(),
                                    SignInView.getInstance(),
                                    SignUpView.getInstance(),
                                    AccountView.getInstance(),
                                    AddAccountView.getInstance(),
                                    CodeView.getInstance(),
                                    SettingsView.getInstance()
                            )
                    );

                    Command command = new SwitchToDarkMode(views);

                    if (preferredTheme.equals("high contrast mode")) {
                        command = new SwitchToHighContrastMode(views);
                    }

                    ThemeSwitcher switcher = new ThemeSwitcher(command);
                    switcher.switchTheme();
                }

                ((AccountView) AccountView.getInstance()).getAccountViewController()
                        .addAccounts(Database.getAccounts(Storage.getToken()));
            }
        }
    }
}
