package cypher.enforcers.controllers.codeViewControllers;

import cypher.enforcers.behaviors.ManualInputReader;
import cypher.enforcers.behaviors.interfaces.ReadCodeBehavior;
import cypher.enforcers.code.readers.CodeReader;
import cypher.enforcers.code.readers.CodeReaderFactory;
import cypher.enforcers.code.readers.types.ReaderType;
import cypher.enforcers.data.security.Code;
import cypher.enforcers.models.AccountModel;
import cypher.enforcers.models.CodeModel;
import cypher.enforcers.models.UserModel;
import cypher.enforcers.views.accountview.AccountView;
import cypher.enforcers.views.codeview.CodeView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCode;
import cypher.enforcers.data.entities.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.views.*;
import cypher.enforcers.views.interfaces.Reversible;
import cypher.enforcers.views.codeview.CodeCell;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller for the code view.
 */
public class CodeViewController implements Initializable {

    // Logger for the code view controller.
    private static final Logger logger = LoggerFactory.getLogger(CodeViewController.class);

    // Contains all content to be displayed for this view.
    @FXML
    private BorderPane background;

    // Contains the account name and social media type to be
    // displayed vertically on top of each other.
    @FXML
    private VBox titleSection;

    // Contains the list-view and text field to be displayed on top
    // of each other.
    @FXML
    private VBox left;

    // Contains the buttons to be displayed on top of each other.
    @FXML
    private VBox right;

    // Controls the spacing above the list-view.
    @FXML
    private Region aboveListView;

    // Displays all the codes.
    @FXML
    private ListView<Code> codeListView;

    // Label that says code.
    @FXML
    private Label codesTitle;

    // Controls the spacing above the buttons.
    // (This ensures the spacing is exactly the same as when the
    // project was first submitted, didn't want to make any
    // changes to the UI on my own).
    @FXML
    private Region aboveButtons;

    // Button that says Import Codes.
    @FXML
    private Button importCodes;

    // Button that says Add Code.
    @FXML
    public Button addCode;

    // Main title for this view.
    @FXML
    private Label title;

    // Title for the name of the account.
    @FXML
    private Label usernameTitle;

    // Controls the spacing on the left side of the text field, so that
    // it's aligned with the list-view.
    @FXML
    private Region beforeCodeInput;

    // Text field to add a code.
    @FXML
    private TextField addCodeInput;

    // Controls the spacing between the text field and Add Code button
    // so that they have enough gap which makes the button and text field
    // stay inline with the list-view.
    @FXML
    private Region spaceBetween;

    // This property is used to control the padding on the left side
    // of the list view so that the content can fit when the screen
    // gets too small.
    private final ObjectProperty<Insets> padding = new SimpleObjectProperty<>(new Insets(0, 0, 0, 30));

    // This property controls the padding on the right and left side
    // of the buttons so the content can fit when the screen gets too
    // small.
    private final ObjectProperty<Insets> rightSidePadding = new SimpleObjectProperty<>(new Insets(0, 12, 0, 10));

    // This property is used to control the padding of the main container
    // so that the contents can fit when the screen gets to small.
    private final ObjectProperty<Insets> windowPadding = new SimpleObjectProperty<>(new Insets(5, 5, 5, 5));

    // To log out the current user.
    private UserModel userModel;

    /**
     * Set the user model.
     *
     * @param model The User Model.
     */
    public void setUserModel(UserModel model) {
        this.userModel = model;
    }

    // To interact with the current account.
    private AccountModel accountModel;

    /**
     * Set the account model.
     *
     * @param model The Account Model.
     */
    public void setAccountModel(AccountModel model) {
        this.accountModel = model;
    }

    // To interact with the account's codes.
    private CodeModel codeModel;

    /**
     * Set the code model.
     *
     * @param model The Code Model.
     */
    public void setCodeModel(CodeModel model) {
        this.codeModel = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.paddingProperty().bind(windowPadding);
        left.paddingProperty().bind(padding);
        right.paddingProperty().bind(rightSidePadding);

        codeListView.itemsProperty().bind(codeModel.codesProperty());

        codeModel.currentCodeProperty().bind(codeListView.getSelectionModel().selectedItemProperty());

        accountModel.currentAccountProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.isNull(newValue)) {
                codeModel.loadCodes(newValue.id());
                return;
            }

            codeModel.clear();
        });

        /*
        If a current account is selected, we want to show its type
        and name, otherwise default labels.
         */
        StringBinding bindingForTitle = Bindings.createStringBinding(
                () -> Objects.isNull(accountModel.getCurrentAccount()) ?
                        "Social Media Platform Name\n"
                        : accountModel.getCurrentAccount().socialMediaType() + "\n",
                accountModel.currentAccountProperty()
        );
        title.textProperty().bind(bindingForTitle);

        StringBinding bindingForUsername = Bindings.createStringBinding(
                () -> Objects.isNull(accountModel.getCurrentAccount()) ?
                        "Label"
                        : accountModel.getCurrentAccount().name(),
                accountModel.currentAccountProperty()
        );
        usernameTitle.textProperty().bind(bindingForUsername);

        /*
        Only make import button visible if account supports it.
         */
        BooleanBinding bindingForImports = Bindings.createBooleanBinding(
                () -> Objects.isNull(accountModel.getCurrentAccount()) || !AccountEntity.supportsImport(accountModel.getCurrentAccount().socialMediaType().toLowerCase()),
                accountModel.currentAccountProperty()
        );
        importCodes.disableProperty().bind(bindingForImports);

        /*
        This view would break down if the screen went too large or
        small. I'm not good at UI thus I didn't know how to make it look,
        so for now I just made it look "functional" when the screen
        sizes change. if screen size is the default width and height, then
        I did my best to maintain the spacings that were there when
        this project was submitted, since I didn't want to tamper the UI.
        You are fully free to change these bindings.
         */

        /*
        The text field, add code button, and the space between them
        each get a certain amount of width. By trial and error I
        figured out the values so that each element gets a certain
        percentage of the total width, and the total space is based on the
        width of the list-view. This allows for the proportions to be
        maintained even when the screen size is changed.
         */
        addCodeInput.prefWidthProperty().bind(
                codeListView.widthProperty()
                        .multiply(246.0 / 374.0)
        );

        addCode.prefWidthProperty().bind(
                codeListView.widthProperty()
                        .multiply(96.0 / 374.0)
        );

        spaceBetween.prefWidthProperty().bind(
                codeListView.widthProperty()
                        .multiply(32.0 / 374.0)
        );

        // Allows the Import Codes button to be in-line with the
        // list view to where it was for when this project was
        // submitted.
        aboveButtons.prefHeightProperty().bind(
                codesTitle.heightProperty()
                        .add(aboveListView.heightProperty())
                        .add(34)
        );

        /*
        There are multiple breakpoints I found from trial and error
        and if the width gets smaller than those breakpoints, then
        paddings will adjust so there's more room for the content.
         */

        background.heightProperty().addListener(((observableValue, oldHeight, newHeight) -> {
            if (newHeight.doubleValue() < 395) {
                aboveListView.setMinHeight(Region.USE_COMPUTED_SIZE);

                if (newHeight.doubleValue() < 318) {
                    double newPadding = Math.max(0, 5 - (318 - newHeight.doubleValue()));
                    windowPadding.set(new Insets(newPadding, 5, newPadding, 5));
                    BorderPane.setMargin(titleSection, new Insets(Math.max(0, 10 - (318 - newHeight.doubleValue())), 0, 0, 0));
                } else {
                    windowPadding.set(new Insets(5, 5, 5, 5));
                    BorderPane.setMargin(titleSection, new Insets(10, 0, 0, 0));
                }
            } else {
                aboveListView.setMinHeight(aboveListView.getPrefHeight());
            }
        }));

        background.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 480) {
                padding.set(new Insets(0, 0, 0, Math.max(0, 30 - (480 - newWidth.doubleValue()))));
                beforeCodeInput.prefWidthProperty().unbind();
                beforeCodeInput.setPrefWidth(padding.getValue().getLeft());

                if (newWidth.doubleValue() < 261) {
                    double newPadding = Math.max(0, 5 - (261 - newWidth.doubleValue()));
                    windowPadding.set(new Insets(newPadding, newPadding, newPadding, newPadding));

                    double newMargin = Math.max(0, 10 - (261 - newWidth.doubleValue()));
                    BorderPane.setMargin(titleSection, new Insets(newMargin, 0, 0, 0));
                    titleSection.setPrefHeight(57 + 200 - newWidth.doubleValue());

                    if (newWidth.doubleValue() < 221) {
                        double delta = 221 - newWidth.doubleValue();
                        rightSidePadding.set(new Insets(0, Math.max(0, 12 - delta), 0, Math.max(0, 10 - delta)));
                    } else {
                        rightSidePadding.set(new Insets(0, 12, 0, 10));
                    }
                } else {
                    windowPadding.set(new Insets(5, 5, 5, 5));
                    BorderPane.setMargin(titleSection, new Insets(10, 0, 0, 0));
                    titleSection.setPrefHeight(57);
                }
            } else {
                padding.set(new Insets(0, 0, 0, 30));
                beforeCodeInput.setPrefWidth(30);
            }
        }));

        codeListView.setCellFactory(test -> {
            try {
                CodeCell cell = new CodeCell(codeListView);
                cell.setCodeModel(codeModel);
                return cell;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Handles the event where the "Import Codes" button is clicked
     * This method allows the user to specify the path to the corresponding
     * .txt file that contains the codes they would like to add.
     */
    public void importCodeOnAction() {
        // 1) Get the platform for the account
        String AccountType = accountModel.getCurrentAccount().socialMediaType().toUpperCase();

        // 2) select a reader based on the corresponding account type
        CodeReader reader = CodeReaderFactory.makeCodeReader(ReaderType.valueOf(AccountType));

        if (reader == null) {
            logger.warn("No reader of type {}. Aborting request.", AccountType);
            return;
        }

        // 3) open the file explorer
        FileChooser chooser = new FileChooser();
        File pathway = chooser.showOpenDialog(CodeView.getInstance().getRoot().getScene().getWindow());

        if (pathway == null) {
            logger.warn("No file was selected. Aborting request");
            return;
        }

        // 4) get the path to the specified file
        String filepath = pathway.getPath();

        // 5) Read the file using the corresponding reader
        reader.setFilePath(filepath);
        addCodes((ReadCodeBehavior) reader);
    }

    /**
     * Handles the event when the "delete all codes" button is clicked.
     * This method clears the items in the listview.
     */
    public void deleteAllOnAction() {
        codeModel.deleteAllCodes(accountModel.getCurrentAccount());
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code to the listview.
     */
    public void addCodeOnAction() {
        addCodes(new ManualInputReader(addCodeInput));
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code using the enter button.
     */
    public void addCodeOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addCodes(new ManualInputReader(addCodeInput));
        }
    }

    /**
     * Add list of codes based on the behavior.
     *
     * @param behavior Obtains a backup code depending on the source.
     */
    private void addCodes(ReadCodeBehavior behavior) {
        List<String> returned = behavior.readCodes();
        for (String s : returned) {
            // Don't want to add empty codes.
            if (!s.isEmpty()) {
                if (!codeModel.addCode(accountModel.getCurrentAccount(), s)) {
                    return;
                }
            }
        }

        addCodeInput.setText("");
    }

    /**
     * Allows a user to logout and redirects them to the home page.
     */
    public void handleLogout() {
        if (userModel.logOutUser()) {
            logger.trace("Switching from the CodeView to the HomePageView.");
            View.switchSceneTo(CodeView.getInstance(), HomePageView.getInstance());
        }
    }

    /**
     * Switches the scene to the SettingsView once the settings button is clicked.
     */
    public void handleSettings() {
        logger.debug("Setting the previous scene for the SettingsView to the CodeView.");
        ((Reversible) SettingsView.getInstance()).setPreviousView(CodeView.getInstance());

        logger.trace("Switching from the CodeView to the SettingsView.");
        View.switchSceneTo(CodeView.getInstance(), SettingsView.getInstance());
    }

    /**
     * Switches the scene to the Account-Viewer view once the back button is clicked.
     */
    public void handleGoBack() {
        logger.trace("Switching from CodeView to AccountView.");
        View.switchSceneTo(CodeView.getInstance(), AccountView.getInstance());
    }
}
