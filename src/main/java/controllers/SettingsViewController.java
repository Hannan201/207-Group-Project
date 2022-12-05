package controllers;

import commands.Command;
import commands.SwitchToDarkMode;
import commands.SwitchToHighContrastMode;
import commands.SwitchToLightMode;
import commands.managers.ThemeSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import org.controlsfx.control.HyperlinkLabel;
import views.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
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
    @FXML
    private HyperlinkLabel copyright;

    private static final String COPYRIGHT =

            "Special thanks to [Icons8] for the following icons: " +
            "[app] icon, [Google] icon, [Discord]icon, [Shopify] icon, " +
            "[Github] icon, [Settings] icon, [Log Out] icon, [Back Arrow] icon.";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        copyright.setText(COPYRIGHT);

        views = new ArrayList<>(List.of(SignUpView.getInstance(), AccountView.getInstance(),
                AddAccountView.getInstance(), CodeView.getInstance(), HomePageView.getInstance()));
        lightModeCommand = new SwitchToLightMode(views);
        darkModeCommand = new SwitchToDarkMode(views);
        highContrastModeCommand = new SwitchToHighContrastMode(views);
        switcher = new ThemeSwitcher(lightModeCommand);

        // switch to light mode as default
        this.switcher.switchTheme();
    }

    public void handleLinkClick(ActionEvent e) throws IOException, URISyntaxException {

        Hyperlink link = (Hyperlink) e.getSource();
        final String str = link == null ? "" : link.getText();
        switch(str) {
            case "Icons8":
                String url = "https://icons8.com";
                Desktop.getDesktop().browse(new URL(url).toURI());
                break;
            case "app":
                String app = "https://icons8.com/icon/4SBCvFZBi2Rc/app";
                Desktop.getDesktop().browse(new URL(app).toURI());
                break;
            case "Google":
                String google = "https://icons8.com/icon/60984/google";
                Desktop.getDesktop().browse(new URL(google).toURI());
                break;
            case "Discord":
                String discord = "https://icons8.com/icon/30888/discord";
                Desktop.getDesktop().browse(new URL(discord).toURI());
                break;
            case "Shopify":
                String shopify = "https://icons8.com/icon/SZ0VDlOvY5zB/shopify";
                Desktop.getDesktop().browse(new URL(shopify).toURI());
                break;
            case "Github":
                String github = "https://icons8.com/icon/62856/github";
                Desktop.getDesktop().browse(new URL(github).toURI());
                break;
            case "Settings":
                String settings = "https://icons8.com/icon/H6C79JoP90DH/settings";
                Desktop.getDesktop().browse(new URL(settings).toURI());
                break;
            case "Back Arrow":
                String log_out = "https://icons8.com/icon/26194/back-arrow";
                Desktop.getDesktop().browse(new URL(log_out).toURI());
                break;
            case "Log Out":
                String back_arrow = "https://icons8.com/icon/O78uUJpfEyFx/log-out";
                Desktop.getDesktop().browse(new URL(back_arrow).toURI());
                break;
        }
    }
}
