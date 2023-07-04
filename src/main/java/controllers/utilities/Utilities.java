package controllers.utilities;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.managers.ThemeSwitcher;
import data.Storage;
import data.database.Database;
import views.*;

import java.util.List;

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
                    CodeView.getInstance()
            );

            Command command = new SwitchToDarkMode(views);

            if (theme.equals("high contrast mode")) {
                command = new SwitchToHighContrastMode(views);
            }

            ThemeSwitcher switcher = new ThemeSwitcher(command);
            switcher.switchTheme();
        }
    }

}
