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
import views.SignInView;
import views.SignUpView;
import views.View;

import java.net.URL;
import java.util.ResourceBundle;

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

    private final static int PREF_WIDTH  = 540;

    private final static int PREF_HEIGHT = 620;

    private static final int TITLE_PREF_WIDTH = 207;
    private static final int TITLE_MAX_WIDTH = 380;

    private static final int BASE_FONT_SIZE = 45;
    private static final int MAX_FONT_SIZE = 85;
    private static final int DEFAULT_FONT_SIZE = 15;

    private static final double FONT_RATIO = (double) DEFAULT_FONT_SIZE / BASE_FONT_SIZE;

    // For the title.
    private final ObjectProperty<Font> titleFontTracking = new SimpleObjectProperty<>(Font.getDefault());
    private final DoubleProperty titleFontSize = new SimpleDoubleProperty();

    // For the buttons.
    private final ObjectProperty<Font> buttonFontTracking = new SimpleObjectProperty<>(Font.font("Montserrat-Regular"));

    private DoubleProperty test = new SimpleDoubleProperty();

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
        titleFontSize.bind(main.widthProperty().add(main.heightProperty())
                .divide(1280 + 720)
                .multiply(100)
                .multiply(45.0 / 58.0)
        );

        main.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            buttonFontTracking.set(Font.font("Montserrat-Regular", result * FONT_RATIO));
        }));

        main.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.doubleValue());
            titleFontTracking.set(Font.font(result));
            buttonFontTracking.set(Font.font("Montserrat-Regular", result * FONT_RATIO));
        }));

        title.fontProperty().bind(titleFontTracking);
        signUp.fontProperty().bind(buttonFontTracking);
        signIn.fontProperty().bind(buttonFontTracking);
        title.prefWidthProperty().bind(test);

        main.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() > PREF_WIDTH) {
                double delta = (newWidth.doubleValue() - PREF_WIDTH) / 6.0;
                test.set(Math.min(TITLE_MAX_WIDTH, TITLE_PREF_WIDTH + delta));
            } else if (newWidth.doubleValue() < PREF_WIDTH) {
                double delta = (PREF_WIDTH - newWidth.doubleValue()) / 3.0;
                test.set(TITLE_PREF_WIDTH - delta);
            } else {
                test.set(TITLE_PREF_WIDTH);
            }
        }));

        main.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            if (newHeight.doubleValue() > PREF_HEIGHT) {
                double delta = (newHeight.doubleValue() - PREF_HEIGHT) / 5.0;
                test.set(Math.min(TITLE_MAX_WIDTH, test.getValue() + delta));
            } else if (newHeight.doubleValue() < PREF_HEIGHT) {
                double delta = (PREF_HEIGHT - newHeight.doubleValue()) / 5.0;
                test.set(test.getValue() - delta);
            } else {
                test.set(TITLE_PREF_WIDTH);
            }
        }));


        space.prefWidthProperty().bind(title.widthProperty().subtract(signIn.widthProperty()).subtract(signUp.widthProperty()));
    }
}
