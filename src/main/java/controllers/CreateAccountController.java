package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import user.Account;
import views.AccountView;
import views.AddAccountView;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable{

    private final Validator validator = new Validator();

    public Button createAccount;

    @FXML
    private HBox icons;

    @FXML
    public ToggleButton github;

    @FXML
    public ToggleButton google;

    @FXML
    public ToggleButton shopify;

    @FXML
    public ToggleButton discord;

    @FXML
    private HBox platformRow;

    @FXML
    private Label platformLabel;

    @FXML
    public TextField platform;

    @FXML
    private HBox usernameRow;

    @FXML
    private Label usernameLabel;

    @FXML
    public TextField username;

    @FXML
    public VBox box;

    // To dynamically calculate the spacing between the
    // social media icons.
    private final DoubleProperty spacing = new SimpleDoubleProperty(10);


    // Don't want the font to be too large.
    private final int MAX_FONT_SIZE = 50;

    // To change the font size of the label.
    private final ObjectProperty<Font> labelFontTracking = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the font size needed, of the labels.
    private final DoubleProperty labelFontSize = new SimpleDoubleProperty();

    // To dynamically calculate the width/height size needed,
    // of the platform and username text fields.
    private final DoubleProperty fieldWidthSize = new SimpleDoubleProperty(157);
    private final DoubleProperty fieldHeightSize = new SimpleDoubleProperty(31);

    // To dynamically calculate the padding needed, of the button
    // based on the font size.
    private final ObjectProperty<Insets> buttonPaddingSize = new SimpleObjectProperty<>(new Insets(5, 26.5, 5.5, 26));

    // To dynamically calculate the size needed, of the button
    // for the icons based on the font size.
    private final DoubleProperty iconButtonSize = new SimpleDoubleProperty(50);

    private final DoubleProperty platformRowSpacing = new SimpleDoubleProperty(71);
    private final DoubleProperty usernameRowSpacing = new SimpleDoubleProperty(19);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        box.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        newWindow.setOnCloseRequest((windowEvent -> {
                            platform.clear();
                            username.clear();
                        }));
                    }
                }));
            }
        }));

        createAccount = new Button("Create Account");
        createAccount.setContentDisplay(ContentDisplay.CENTER);
        createAccount.setMinSize(
                Button.USE_COMPUTED_SIZE,
                Button.USE_COMPUTED_SIZE
        );
        createAccount.setPrefSize(
                Button.USE_COMPUTED_SIZE,
                Button.USE_COMPUTED_SIZE
        );
        createAccount.setMaxSize(
                Button.USE_COMPUTED_SIZE,
                Button.USE_COMPUTED_SIZE
        );
        createAccount.paddingProperty().bind(buttonPaddingSize);
        createAccount.fontProperty().bind(labelFontTracking);
        // creates the decorated button
        TooltipWrapper<Button> createAccountWrapper = new TooltipWrapper<>(
                createAccount,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot add account:\n", validator.createStringBinding()));
        // adds the Account to the ListView in AccountsView when the button is clicked
        createAccount.setOnAction(c -> {
            Account account = new Account(username.getText(), platform.getText());
            ((AccountView)AccountView.getInstance()).getAccountViewController().addAccount(account);
            Stage stage = (Stage) AddAccountView.getInstance().getRoot().getScene().getWindow();
            stage.close();
            username.clear();
            platform.clear();
        });

        box.getChildren().add(createAccountWrapper); // adds the decorated button to the HBox

        // Renders a button un-clickable and adds a hover message over the button
        // if there exists an Account with the same username and platform

        validator.createCheck()
                .withMethod(c -> {
                    Account account = new Account(username.getText(), platform.getText());
                    boolean duplicate = ((AccountView)AccountView.getInstance()).getAccountViewController().existsDuplicate(account);
                    if (duplicate && !(username.getText().equals("") || platform.getText().equals(""))){
                        c.error("This account already exists, please try again!");
                    }
                })
                .dependsOn("username", username.textProperty())
                .dependsOn("platform", platform.textProperty())
                .decorates(username)
                .decorates(platform)
                .immediate()
        ;

        // Renders a button un-clickable and adds a hover message over the button
        // if the text in the username TextField is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (username.getText().equals("")){
                        c.error("Empty username/email.");
                    }
                })
                .dependsOn("username", username.textProperty())
                .decorates(username)
                .immediate()
        ;

        // Renders a button un-clickable and adds a hover message over the button
        // if the text in the platform TextField is empty

        validator.createCheck()
                .withMethod(c -> {
                    if (platform.getText().equals("")){
                        c.error("Empty platform.");
                    }
                })
                .dependsOn("platform", platform.textProperty())
                .decorates(platform)
                .immediate()
        ;

        icons.spacingProperty().bind(spacing);

        labelFontSize.bind(
                box.heightProperty()
                        .add(box.widthProperty())
                        .divide(2000.0)
                        .multiply(100.0)
                        .multiply(15.0 / 30.5)
        );

        box.widthProperty().addListener((observableValue, number, t1) -> {
            double result = Math.min(
                    MAX_FONT_SIZE,
                    labelFontSize.getValue()
            );
            labelFontTracking.set(Font.font(result));
            fieldWidthSize.set(result * (140.0 / 15.0));
            fieldHeightSize.set(result * (31.0 / 15.0));
            platformRowSpacing.set(result * (71.0 / 15.0));
            usernameRowSpacing.set(result * (19.0 / 15.0));
            buttonPaddingSize.set(
                    new Insets(
                            result * (1.0 / 3.0),
                            result * (26.5 / 15.0),
                            result * (5.5 / 15.0),
                            result * (26.0 / 15.0)
                    )
            );
            iconButtonSize.set(result * (50.0 / 15.0));
            VBox.setMargin(
                    icons,
                    new Insets(
                            0,
                            0,
                            result * 0.3,
                            0
                    )
            );
            VBox.setMargin(
                    platformRow,
                    new Insets(
                            result * (4.0 / 15.0),
                            0,
                            0,
                            0
                    )
            );
            VBox.setMargin(
                    usernameRow,
                    new Insets(
                            0,
                            0,
                            result * (7.75 / 15.0),
                            0
                    )
            );
            VBox.setMargin(
                    createAccountWrapper,
                    new Insets(
                            result * (7.75 / 15.0),
                            0,
                            0,
                            0
                    )
            );
            spacing.set(result * (10.0 / 15.0));
        });

        box.heightProperty().addListener((observableValue, number, t1) -> {
            double result = Math.min(
                    MAX_FONT_SIZE,
                    labelFontSize.getValue()
            );
            labelFontTracking.set(Font.font(result));
            fieldWidthSize.set(result * (140.0 / 15.0));
            fieldHeightSize.set(result * (31.0 / 15.0));
            platformRowSpacing.set(result * (71.0 / 15.0));
            usernameRowSpacing.set(result * (19.0 / 15.0));
            buttonPaddingSize.set(
                    new Insets(
                            result * (1.0 / 3.0),
                            result * (26.5 / 15.0),
                            result * (5.5 / 15.0),
                            result * (26.0 / 15.0)
                    )
            );
            iconButtonSize.set(result * (50.0 / 15.0));
            VBox.setMargin(
                    icons,
                    new Insets(
                            0,
                            0,
                            result * 0.3,
                            0
                    )
            );
            VBox.setMargin(
                    platformRow,
                    new Insets(
                            result * (4.0 / 15.0),
                            0,
                            0,
                            0
                    )
            );
            VBox.setMargin(
                    usernameRow,
                    new Insets(
                            0,
                            0,
                            result * (7.75 / 15.0),
                            0
                    )
            );
            VBox.setMargin(
                    createAccountWrapper,
                    new Insets(
                            result * (7.75 / 15.0),
                            0,
                            0,
                            0
                    )
            );
            spacing.set(result * (10.0 / 15.0));
        });

        icons.spacingProperty().bind(spacing);

        platformLabel.fontProperty().bind(labelFontTracking);

        usernameLabel.fontProperty().bind(labelFontTracking);

        platform.prefWidthProperty().bind(fieldWidthSize);
        platform.prefHeightProperty().bind(fieldHeightSize);
        platform.fontProperty().bind(labelFontTracking);

        username.prefWidthProperty().bind(fieldWidthSize);
        username.prefHeightProperty().bind(fieldHeightSize);
        username.fontProperty().bind(labelFontTracking);

        platformRow.spacingProperty().bind(
                platformRowSpacing
        );
        usernameRow.spacingProperty().bind(
                usernameRowSpacing
        );

        github.prefWidthProperty().bind(iconButtonSize);
        github.prefHeightProperty().bind(iconButtonSize);

        google.prefWidthProperty().bind(iconButtonSize);
        google.prefHeightProperty().bind(iconButtonSize);

        shopify.prefWidthProperty().bind(iconButtonSize);
        shopify.prefHeightProperty().bind(iconButtonSize);

        discord.prefWidthProperty().bind(iconButtonSize);
        discord.prefHeightProperty().bind(iconButtonSize);
    }

    /**
     * Toggle the platform text field to make it un-editable
     * if it's editable and attach text to it, and editable
     * if it's un-editable which will clear the text.
     *
     * @param text Message to display if the text field is
     *             un-editable.
     */
    public void toggle(String text) {
        platform.setText(platform.isEditable() ? text : "");
        platform.setEditable(!platform.isEditable());
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the GitHub toggle button is pressed.
     */
    public void handleGithub() {
        toggle("Github");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Google toggle button is pressed.
     */
    public void handleGoogle() {
        toggle("Google");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the shopify toggle button is pressed.
     */
    public void handleShopify() {
        toggle("Shopify");
    }

    /**
     * A handle method which sets the text of the platform textField
     * if the Discord toggle button is pressed.
     */
    public void handleDiscord() {
        toggle("Discord");
    }
}
