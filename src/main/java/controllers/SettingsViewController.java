package controllers;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.SwitchToLightMode;
import commands.managers.ThemeSwitcher;
import javafx.fxml.Initializable;
import views.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {

    private static List<View> views;

    private ThemeSwitcher switcher;

    private Command lightModeCommand;

    private Command darkModeCommand;

    private Command highContrastModeCommand;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        views = new ArrayList<>(List.of(SignUpView.getInstance(), AccountView.getInstance(),
                AddAccountView.getInstance(), CodeView.getInstance(), HomePageView.getInstance()));
        lightModeCommand = new SwitchToLightMode(views);
        darkModeCommand = new SwitchToDarkMode(views);
        highContrastModeCommand = new SwitchToHighContrastMode(views);
        switcher = new ThemeSwitcher(lightModeCommand);

        // switch to light mode as default
        this.switcher.switchTheme();
    }
}
