package controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import views.SignInView;
import views.SignUpView;
import views.View;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomePageController implements Initializable {

    @FXML
    private Button signIn;

    @FXML
    private Button signUp;

    @FXML
    private Label title;

    @FXML
    private Region space;

    @FXML
    private BorderPane main;

    // Preferred height and width of the
    // window. So the width and height can be restored
    // to their default values.
    private final int PREF_WIDTH  = 540;
    private final int PREF_HEIGHT = 620;

    // This is needed to adjust the width and height
    // of the title so each word is on a new line.
    private final int TITLE_PREF_WIDTH = 207;
    private final int TITLE_MAX_WIDTH = 380;

    // Default font size of the title when application
    // launches.
    private final int BASE_FONT_SIZE = 45;

    // Don't want the font to be too large.
    private final int MAX_FONT_SIZE = 85;

    // Font size of the buttons.
    private final int DEFAULT_FONT_SIZE = 15;

    // To ensure the title's and button's font size grow
    // proportionally.
    private final double FONT_RATIO = (double) DEFAULT_FONT_SIZE / BASE_FONT_SIZE;

    // For the title.
    private final ObjectProperty<Font> titleFontTracking = new SimpleObjectProperty<>(Font.getDefault());
    private final DoubleProperty titleFontSize = new SimpleDoubleProperty();

    // For the buttons.
    private final ObjectProperty<Font> buttonFontTracking = new SimpleObjectProperty<>(Font.font("Montserrat-Regular"));

    // For the title's width so each word is on a new line.
    private final DoubleProperty titleWidthSize = new SimpleDoubleProperty();

    // The amount of pixels before the title's width can change.
    // In other words, the width/height will need to be changed
    // by at least 6 pixels before the width will adjust.
    private final double PIXEL_GROWTH_FACTOR = 6.0;

    /**
     * A handle method for the Sign-In button that opens a pop-up to allow the
     * user to sign-in.
     */
    public void handleSignIn() {
        View.loadNewWindow(SignInView.getInstance());
    }

    /**
     * A handle method for the Sign-Up button that opens a pop-up to allow the
     * user to sign in.
     */
    public void handleSignUp() {
        View.loadNewWindow(SignUpView.getInstance());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Wait for the window to load.
        main.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        initializeBindings((Stage) newWindow);
                    }
                }));
            }
        });
    }

    /**
     * Configure all the bindings based on the window's width
     * and height.
     *
     * @param stage Window.
     */
    public void initializeBindings(Stage stage) {
        // To see if the window was just maximised or not.
        AtomicBoolean maximised = new AtomicBoolean(false);
        double[] previousWidth = { 0 };

        titleFontSize.bind(main.widthProperty().add(main.heightProperty())
                .divide(1280 + 720)
                .multiply(100)
                .multiply(45.0 / 58.0)
        );

        main.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            buttonFontTracking.set(Font.font(result * FONT_RATIO));
        }));

        main.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            buttonFontTracking.set(Font.font(result * FONT_RATIO));
        }));

        titleWidthSize.set(TITLE_PREF_WIDTH);
        title.fontProperty().bind(titleFontTracking);
        signUp.fontProperty().bind(buttonFontTracking);
        signIn.fontProperty().bind(buttonFontTracking);
        title.prefWidthProperty().bind(titleWidthSize);

        // When the window changes, the width change listener
        // will first be queued, then the height change listener.
        main.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (stage.isMaximized()) {
                previousWidth[0] = titleWidthSize.getValue();
                maximised.set(true);
            } else if (maximised.get() && !stage.isMaximized()) {
                titleWidthSize.set(previousWidth[0]);
                return;
            }

            if (newWidth.doubleValue() != PREF_WIDTH) {
                double delta = (oldWidth.doubleValue() - newWidth.doubleValue()) / PIXEL_GROWTH_FACTOR;
                titleWidthSize.set(Math.min(TITLE_MAX_WIDTH, titleWidthSize.getValue() - delta));
            } else {
                titleWidthSize.set(TITLE_PREF_WIDTH);
            }
        }));

        main.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            if (stage.isMaximized()) {
                return;
            } else if (maximised.get() && !stage.isMaximized()) {
                maximised.set(false);
                return;
            }

            if (newHeight.doubleValue() != PREF_HEIGHT) {
                double delta = (oldHeight.doubleValue() - newHeight.doubleValue()) / PIXEL_GROWTH_FACTOR;
                titleWidthSize.set(Math.min(TITLE_MAX_WIDTH, titleWidthSize.getValue() - delta));
            } else {
                titleWidthSize.set(TITLE_PREF_WIDTH);
            }
        }));

        space.prefWidthProperty().bind(title.widthProperty().subtract(signIn.widthProperty()).subtract(signUp.widthProperty()));
    }
}
