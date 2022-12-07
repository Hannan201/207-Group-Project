import commands.Command;
import commands.SwitchToHighContrastMode;
import commands.managers.ThemeSwitcher;
import data.Database;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.User;
import views.*;

import java.util.ArrayList;
import java.util.List;

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
            Database.saveUserData();
        });

        boolean status = Database.getLoginStatus();
        String preferredTheme = "";

        // User left application without logging out.
        if (status) {
            Parent root = AccountView.getInstance().getRoot();
            User user = Database.getUser();
            if (user != null) {
                ((AccountView) AccountView.getInstance()).getAccountViewController().addAccounts(user.getAccounts());
                preferredTheme = user.getCurrentTheme();
            }
            Scene scene = new Scene(root);

            // Switch to the respective theme.
            if (!preferredTheme.equals("Light")) {
                List<View> views = new ArrayList<>(List.of(HomePageView.getInstance(), SignInView.getInstance(),
                                                SignInView.getInstance(), AccountView.getInstance(), AddAccountView.getInstance(),
                                                CodeView.getInstance(), SettingsView.getInstance()));

                if (preferredTheme.equals("High Contrast")) {
                    Command command = new SwitchToHighContrastMode(views);
                    ThemeSwitcher switcher = new ThemeSwitcher(command);
                    switcher.switchTheme();
                }
            }

            scene.getStylesheets().add(AccountView.getInstance().getCurrentThemePath());
            stage.setScene(scene);
        } else {
            Parent root = HomePageView.getInstance().getRoot();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(HomePageView.getInstance().getCurrentThemePath());
            stage.setScene(scene);
        }


        stage.show();
    }
}
