package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import views.SignInView;
import views.SignUpView;
import views.View;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class HomePageController implements Initializable {

    @FXML
    private Button signIn;

    @FXML
    private Button signUp;

    @FXML
    private HBox buttons;

    @FXML
    private Label title;

    @FXML
    private Region space;

    @FXML
    private BorderPane main;

    @FXML
    private Region above;

    @FXML
    private Region below;

    DoubleProperty test = new SimpleDoubleProperty();
    DoubleProperty factor = new SimpleDoubleProperty();


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

    private ObjectExpression<Font> fontTracking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontTracking = Bindings.createObjectBinding(() -> Font.font("Montserrat", (((main.getWidth() + main.getHeight()) / (2000.0)) * 100.0) * (45.0 / 58.0)), main.widthProperty(), main.heightProperty());
        title.fontProperty().bind(fontTracking);

        below.prefHeightProperty().bind(main.heightProperty().subtract(title.prefHeightProperty()).subtract(buttons.heightProperty()).divide(2).add(17));
        above.prefHeightProperty().bind(main.heightProperty().subtract(title.prefHeightProperty()).subtract(buttons.heightProperty()).divide(2).subtract(17));
        space.prefWidthProperty().bind(title.widthProperty().subtract(signIn.widthProperty()).subtract(signUp.widthProperty()));
        boolean[] belowLimit = {false};

        main.widthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() < title.getPrefWidth() - 45) {
                factor.set(title.getPrefWidth() - t1.doubleValue());
                if (!belowLimit[0]) {
                    belowLimit[0] = true;
                    above.prefHeightProperty().bind(test.subtract(factor));
                    below.prefHeightProperty().bind(test.subtract(factor));
                }
            } else {
                test.set(below.prefHeightProperty().getValue());
                if (belowLimit[0]) {
                    belowLimit[0] = false;
                    below.prefHeightProperty().bind(main.heightProperty().subtract(title.prefHeightProperty()).subtract(signUp.heightProperty()).divide(2));
                    above.prefHeightProperty().bind(main.heightProperty().subtract(title.prefHeightProperty()).subtract(signUp.heightProperty()).divide(2));
                }
            }
        });
    }
}
