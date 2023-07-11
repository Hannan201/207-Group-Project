package utilities;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.managers.ThemeSwitcher;
import data.Storage;
import data.database.Database;
import views.*;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/*
 This class contains utility methods used amongst the controllers.
 */
public class Utilities {

    /**
     * Load all the accounts to the account view. Ideally called when
     * the scene is being switched to the account view.
     */
    public static void loadAccounts() {
        ((AccountView) AccountView.getInstance()).getAccountViewController()
                .addAccounts(Database.getAccounts(Storage.getToken()));
    }

    /**
     * Adjust the theme for a user.
     */
    public static void adjustTheme() {
        String theme = Database.getTheme(Storage.getToken());
        if (theme != null && !theme.equals("light mode")) {
            List<View> views = List.of(
                    HomePageView.getInstance(),
                    SignUpView.getInstance(),
                    AccountView.getInstance(),
                    AddAccountView.getInstance(),
                    CodeView.getInstance(),
                    SettingsView.getInstance()
            );

            Command command = new SwitchToDarkMode(views);

            if (theme.equals("high contrast mode")) {
                command = new SwitchToHighContrastMode(views);
            }

            ThemeSwitcher switcher = new ThemeSwitcher(command);
            switcher.switchTheme();
        }
    }

    /**
     * Load a file from the resource folder in the format of
     * a URL.
     *
     * @param path Path to the file relative to the resource folder.
     */
    public static URL loadFileByURL(String path) {
        return Objects.requireNonNull(
                Utilities.class.getClassLoader()
                        .getResource(path)
        );
    }

    /**
     * Return true if a string is an integer.
     *
     * @param s The string.
     * @return True if it is a integer, false otherwise.
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }

        return true;
    }
}