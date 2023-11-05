package cypher.enforcers.controllers;

import cypher.enforcers.models.UserModel;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.*;
import cypher.enforcers.views.accountview.AccountView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the home page view.
 */
public class HomePageController implements Initializable {

    // Logger for the home page view.
    private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

    // Container to hold all elements to be showed for this view.
    @FXML
    private BorderPane main;

    // Button that says sign-up.
    @FXML
    private Button signIn;

    // Button that says sign-in.
    @FXML
    private Button signUp;

    // Container that holds the buttons horizontally.
    @FXML
    private HBox buttons;

    // Title that says "Backup Code Generator".
    @FXML
    private Label title;

    // Controls the spacing above the title.
    @FXML
    private Region above;

    // Controls the spacing below the sign-up and sign-in buttons.
    @FXML
    private Region below;

    // This is used as a backup property to store the original
    // spacing above and below to center the label and buttons for if
    // the window's width becomes smaller than the title's text width.
    private final DoubleProperty originalSpacing = new SimpleDoubleProperty();

    // If the window's width is less than the width of the title's text,
    // then this property is used to store the difference between the
    // two values.
    private final DoubleProperty delta = new SimpleDoubleProperty();

    // Used to check if a user's already logged in.
    private UserModel userModel;

    /**
     * Set the user model.
     *
     * @param model The User Model.
     */
    public void setUserModel(UserModel model) {
        this.userModel = model;
    }

    /**
     * A handle method for the Sign-In button that opens a pop-up to allow the
     * user to sign-in.
     *
     * @throws IOException if any errors occur while loading in the views.
     */
    public void handleSignIn() throws IOException {
        logger.trace("Engaging SignInView window.");
        View.loadNewWindow(SignInView.getInstance());
    }

    /**
     * A handle method for the Sign-Up button that opens a pop-up to allow the
     * user to sign in.
     *
     * @throws IOException if any errors occur while loading in the views.
     */
    public void handleSignUp() throws IOException {
        logger.trace("Engaging SignUpView window.");
        View.loadNewWindow(SignUpView.getInstance());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!Objects.isNull(userModel.getCurrentUser())) {
            // Switch to account view if user's still logged in.
            main.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
                if (oldScene == null && newScene != null) {
                    newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                        if (oldWindow == null && newWindow != null) {
                            try {
                                Utilities.adjustTheme(userModel.getCurrentUser().theme());
                                View.switchSceneTo(HomePageView.getInstance(), AccountView.getInstance());
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        }
                    }));
                }
            }));
        }

        // To close the database connection when window closes.
        main.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener(((observableValue1, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        newScene.getWindow()
                                .addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> userModel.shutDown());
                    }
                }));
            }
        }));

        // The spacings above the title and below the buttons were not
        // equal when this project was first submitted. Since I wasn't
        // responsible for the UI and didn't want to tamper with to
        // too much, I made it so the original spacings were maintained.
        // You can change them if you wish - by Hannan201.

        below.prefHeightProperty().bind(
                main.heightProperty()
                        .subtract(title.prefHeightProperty())
                        .subtract(buttons.heightProperty())
                        .divide(2)
                        .add(17) // Random value found from trial and
                                 // error to maintain original spacing
                                 // for when this project was first
                                 // submitted.
        );

        above.prefHeightProperty().bind(
                main.heightProperty()
                        .subtract(title.prefHeightProperty())
                        .subtract(buttons.heightProperty())
                        .divide(2)
                        .subtract(17) // Random value found from trial and
                                           // error to maintain original spacing
                                           // for when this project was first
                                           // submitted.
        );

        // This makes it so the spacing between the sign-up and sign-in
        // button changes if the width of the title changes. For example,
        // if the screen width becomes too small.
        buttons.spacingProperty().bind(
                title.widthProperty()
                        .subtract(signIn.widthProperty())
                        .subtract(signUp.widthProperty())
        );

        // This is used to track if the window's current width is
        // less than the title's text width. Java doesn't allow
        // using boolean variables inside a lambda function, so I had
        // to make it an array.
        boolean[] belowLimit = {false};

        main.widthProperty().addListener((observableValue, number, t1) -> {
            /*
               The title has some extra spacing on its right side, so the
               actual width of the text itself is actually less than the
               label's width. After trial and error, I figured the value
               is close to 45. You can change this if you wish.

               When the window's width is less than the width of the
               title's text, then the spacing above the title and below
               the buttons will shrink, this gives the title more room
               to expand its height and thus be able to wrap the text.

               The boolean is used to control if the window's width is
               smaller or not, this avoids repeating the bindings to adjust
               the spacing above and below.
             */

            if (t1.doubleValue() < title.getPrefWidth() - 45) {
                delta.set(title.getPrefWidth() - t1.doubleValue());
                if (!belowLimit[0]) {
                    belowLimit[0] = true;
                    above.prefHeightProperty().bind(
                            originalSpacing.subtract(delta)
                    );
                    below.prefHeightProperty().bind(
                            originalSpacing.subtract(delta)
                    );
                }
            } else {
                originalSpacing.set(below.prefHeightProperty().getValue());
                if (belowLimit[0]) {
                    belowLimit[0] = false;
                    below.prefHeightProperty().bind(
                            main.heightProperty()
                                    .subtract(title.prefHeightProperty())
                                    .subtract(signUp.heightProperty())
                                    .divide(2)
                                    .add(17)
                    );
                    above.prefHeightProperty().bind(
                            main.heightProperty()
                                    .subtract(title.prefHeightProperty())
                                    .subtract(signUp.heightProperty())
                                    .divide(2)
                                    .subtract(17)
                    );
                }
            }
        });
    }
}