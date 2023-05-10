package controllers.CodeViewControllers;

import behaviors.ManualInputReader;
import behaviors.interfaces.ReadCodeBehavior;
import code.readers.CodeReader;
import code.readers.CodeReaderFactory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCode;
import data.Database;
import user.Account;
import user.User;
import views.*;
import views.interfaces.Reversible;
import views.utilities.CodeViewUtilities.CodeCell;
import views.utilities.CodeViewUtilities.CodeCellFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class CodeViewController implements Initializable {

    @FXML
    private BorderPane background;

    @FXML
    private VBox titleSection;

    @FXML
    private VBox left;

    @FXML
    private VBox right;

    @FXML
    private ListView<CodeCell> codeListView;

    @FXML
    private Label placeholderText;

    @FXML
    private Label codesTitle;

    @FXML
    private Button importCodes;

    @FXML
    private Button deleteAll;

    @FXML
    public Button addCode;

    @FXML
    private Label title;

    @FXML
    private Label usernameTitle;

    @FXML
    private GridPane grid;

    @FXML
    private TextField addCodeInput;

    private Account account;

    @FXML
    private HBox manualInputRow;

    @FXML
    private HBox buttonsRow;

    @FXML
    public Button back;

    @FXML
    public Button logout;

    @FXML
    public Button settings;

    private static final List<String> SUPPORTED_PLATFORMS = new ArrayList<>(List.of("shopify", "discord", "google", "github"));

    // Don't want the font to be too large.
    private final int MAX_FONT_SIZE = 30;

    // To change the font size of the title.
    private final ObjectProperty<Font> titleFontTracking = new SimpleObjectProperty<>(Font.getDefault());

    // To dynamically calculate the font size needed, of the title.
    private final DoubleProperty titleFontSize = new SimpleDoubleProperty();

    // To dynamically calculate the font size needed, of the buttons.
    private final ObjectProperty<Font> buttonFontSize = new SimpleObjectProperty<>(Font.getDefault());

    private final ObjectProperty<Insets> codesTitlePaddingSize = new SimpleObjectProperty<>(new Insets(4, 0, 4, 5));

    private final ObjectProperty<Insets> importCodesPaddingSize = new SimpleObjectProperty<>(new Insets(23, 20, 23, 19));
    private final ObjectProperty<Insets> deleteAllPaddingSize = new SimpleObjectProperty<>(new Insets(23, 10, 23, 10));

    private final DoubleProperty manualInputRowSpacing = new SimpleDoubleProperty(25);

    private final ObjectProperty<Insets> addCodePaddingSize = new SimpleObjectProperty<>(new Insets(3.5, 13.5, 3.5, 13));

    private final ObjectProperty<Insets> bottomButtonsPadding = new SimpleObjectProperty<>(new Insets(6, 16, 6, 16.5));
    private final ObjectProperty<Insets> rowPadding = new SimpleObjectProperty<>(new Insets(4, 0, 12, 0));
    private final DoubleProperty rowSpacing = new SimpleDoubleProperty(170);

    private final ObjectProperty<Font> noItemsFontSize = new SimpleObjectProperty<>(Font.getDefault());

    private final DoubleProperty leftSpacing = new SimpleDoubleProperty(9);
    private final ObjectProperty<Insets> leftPadding = new SimpleObjectProperty<>(new Insets(0, 0, 0, 30));

    private final DoubleProperty rightMargin = new SimpleDoubleProperty(34);
    private final ObjectProperty<Insets> rightPadding = new SimpleObjectProperty<>(new Insets(0, 28, 0, 28));

    private final DoubleProperty listViewWidth = new SimpleDoubleProperty(352);

    private final ObjectProperty<Insets> windowPadding = new SimpleObjectProperty<>(new Insets(10, 5, 0, 5));

    private final DoubleProperty listViewBaseSize = new SimpleDoubleProperty();

    private final ObjectProperty<Font> listViewButtonFontSize = new SimpleObjectProperty<>(Font.getDefault());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleFontSize.bind(
                background.widthProperty()
                          .add(background.heightProperty())
                          .divide(1280 + 720)
                          .multiply(100)
                          .multiply(20.0 / 58.0)
        );

        background.widthProperty().addListener((observableValue, number, t1) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.getValue());
            titleFontTracking.set(Font.font(result));
            buttonFontSize.set(Font.font(result * 0.75));
            codesTitlePaddingSize.set(new Insets(result / 5.0, 0, result / 5.0, result / 5.0));
            double verticalButtonPadding = result * 1.15;
            importCodesPaddingSize.set(new Insets(verticalButtonPadding, result, verticalButtonPadding, result * 0.95));
            deleteAllPaddingSize.set(new Insets(verticalButtonPadding, result / 2.0, verticalButtonPadding, result / 2.0));
            rowPadding.set(new Insets(result * 0.35, 0, result * 0.6, 0));
            BorderPane.setMargin(buttonsRow, new Insets(0, result * 2.1, 0, result * 2.125));
            bottomButtonsPadding.set(new Insets(result * 0.3, result * 0.8, result * 0.3, result * 0.825));
            rowSpacing.set(result * 8.5);
            noItemsFontSize.set(Font.font(result * 1.25));
            leftSpacing.set(result * 0.45);
            leftPadding.set(new Insets(0, 0, 0, result * 1.5));
            rightPadding.set(new Insets(0, result * 1.4, 0, result * 1.4));
            listViewWidth.set(result * 17.6);
            addCodePaddingSize.set(new Insets(result * 0.175, result * 0.675, result * 0.175, result * 0.65));
            manualInputRowSpacing.set(result * 1.25);
            windowPadding.set(new Insets(result / 2.0, result / 4.0, 0, result / 4.0));
            BorderPane.setMargin(grid, new Insets(result * 0.45, 0, 0, 0));
            rightMargin.set(result * 1.7);
            if (codesTitle.getHeight() != 0) {
                GridPane.setMargin(
                        right,
                        new Insets(
                            codesTitle.getHeight() + leftSpacing.getValue() + rightMargin.getValue(),
                            0,
                            manualInputRow.getHeight() + leftSpacing.getValue() + rightMargin.getValue(),
                            0
                        )
                );
            }
            listViewBaseSize.set(result);
            listViewButtonFontSize.set(Font.font(result * 0.6));
        });

        background.heightProperty().addListener((observableValue, number, t1) -> {
            double result = Math.min(MAX_FONT_SIZE, titleFontSize.getValue());
            titleFontTracking.set(Font.font(result));
            buttonFontSize.set(Font.font(result * 0.75));
            codesTitlePaddingSize.set(new Insets(result / 5.0, 0, result / 5.0, result / 5.0));
            double verticalButtonPadding = result * 1.15;
            importCodesPaddingSize.set(new Insets(verticalButtonPadding, result, verticalButtonPadding, result * 0.95));
            deleteAllPaddingSize.set(new Insets(verticalButtonPadding, result / 2.0, verticalButtonPadding, result / 2.0));
            rowPadding.set(new Insets(result * 0.35, 0, result * 0.6, 0));
            BorderPane.setMargin(buttonsRow, new Insets(0, result * 2.1, 0, result * 2.125));
            bottomButtonsPadding.set(new Insets(result * 0.3, result * 0.8, result * 0.3, result * 0.825));
            rowSpacing.set(result * 8.5);
            noItemsFontSize.set(Font.font(result * 1.25));
            leftSpacing.set(result * 0.45);
            leftPadding.set(new Insets(0, 0, 0, result * 1.5));
            rightPadding.set(new Insets(0, result * 1.4, 0, result * 1.4));
            listViewWidth.set(result * 17.6);
            addCodePaddingSize.set(new Insets(result * 0.175, result * 0.675, result * 0.175, result * 0.65));
            manualInputRowSpacing.set(result * 1.25);
            windowPadding.set(new Insets(result / 2.0, result / 4.0, 0, result / 4.0));
            BorderPane.setMargin(grid, new Insets(result * 0.45, 0, 0, 0));
            rightMargin.set(result * 1.7);
            if (codesTitle.getHeight() != 0) {
                GridPane.setMargin(
                        right,
                        new Insets(
                            codesTitle.getHeight() + leftSpacing.getValue() + rightMargin.getValue(),
                            0,
                            manualInputRow.getHeight() + leftSpacing.getValue() + rightMargin.getValue(),
                            0
                        )
                );
            }
            listViewBaseSize.set(result);
            listViewButtonFontSize.set(Font.font(result * 0.6));
        });

        title.fontProperty().bind(titleFontTracking);
        usernameTitle.fontProperty().bind(buttonFontSize);

        importCodes.fontProperty().bind(buttonFontSize);
        deleteAll.fontProperty().bind(buttonFontSize);

        importCodes.paddingProperty().bind(importCodesPaddingSize);
        deleteAll.paddingProperty().bind(deleteAllPaddingSize);

        manualInputRow.spacingProperty().bind(manualInputRowSpacing);

        addCodeInput.fontProperty().bind(buttonFontSize);

        addCode.paddingProperty().bind(addCodePaddingSize);
        addCode.fontProperty().bind(buttonFontSize);

        back.paddingProperty().bind(bottomButtonsPadding);
        logout.paddingProperty().bind(bottomButtonsPadding);
        settings.paddingProperty().bind(bottomButtonsPadding);

        placeholderText.fontProperty().bind(noItemsFontSize);

        background.paddingProperty().bind(windowPadding);

        buttonsRow.paddingProperty().bind(rowPadding);
        buttonsRow.spacingProperty().bind(rowSpacing);

        right.paddingProperty().bind(rightPadding);
        right.spacingProperty().bind(
                codeListView.heightProperty()
                        .subtract(rightMargin)
                        .subtract(rightMargin)
                        .subtract(importCodes.heightProperty())
                        .subtract(deleteAll.heightProperty())
                        .subtract(importCodes.heightProperty())
                        .divide(2.0)
        );

        GridPane.setMargin(
                right,
                new Insets(
                     40 + rightMargin.getValue(),
                    0,
                    40 + rightMargin.getValue(),
                    0
                )
        );

        left.spacingProperty().bind(leftSpacing);
        left.paddingProperty().bind(leftPadding);

        codesTitle.fontProperty().bind(buttonFontSize);
        codesTitle.paddingProperty().bind(codesTitlePaddingSize);
        codeListView.prefWidthProperty().bind(listViewWidth);

        codeListView.setCellFactory(test -> {
            try {
                CodeCellFactory codeCellFactory = new CodeCellFactory(codeListView);
                codeCellFactory.setDefaultSizes(
                        listViewBaseSize,
                        listViewButtonFontSize,
                        buttonFontSize
                );
                return codeCellFactory;
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
        importCodes.setOnAction(event -> {
            // 1) Get the platform for the account
            String AccountType = account.getSocialMediaType().toLowerCase();

            // 2) select a reader based on the corresponding account type
            CodeReader reader = CodeReaderFactory.makeCodeReader(AccountType);

            // 3) open the file explorer
            FileChooser chooser = new FileChooser();
            File pathway = chooser.showOpenDialog(CodeView.getInstance().getRoot().getScene().getWindow());

            // 4) get the path to the specified file
            if (!(pathway == null)) {
                if (reader != null) {
                    String filepath = pathway.getPath();

                    // 5) Read the file using the corresponding reader
                    reader.setFilePath(filepath);
                    account.setReadCodeBehavior((ReadCodeBehavior) reader);
                    account.addCodes();
                    List<String> importedCodes = reader.extractCodes(filepath);

                    // 6) Take the List<Strings> that is returned and turn them into CodeCell Objects, which can
                    //    be added into the list view.
                    for (String code : importedCodes) {
                        codeListView.getItems().add(new CodeCell(code));
                    }
                }
            }
        });
    }

    /**
     * Handles the event when the "delete all codes" button is clicked.
     * This method clears the items in the listview.
     */
    public void deleteAllOnAction() {
        deleteAll.setOnAction(e -> {
            codeListView.getItems().clear();
            account.clearUserCodes();
        });
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code to the listview.
     */
    public void addCodeOnAction() {
        addCode.setOnAction(event -> {
            // Don't want to add empty account.
            String newItem = addCodeInput.getText();
            if (!newItem.equals("")) {
                account.setReadCodeBehavior(new ManualInputReader(addCodeInput));
                account.addCodes();
                codeListView.getItems().add(new CodeCell(newItem));
                addCodeInput.setText("");
            }
        });
    }

    /**
     * Handles the event where the "Add Code" button is clicked.
     * This method allows the user to add a specified code using the enter button.
     */
    public void addCodeOnEnter() {
        addCodeInput.setOnKeyPressed(event -> {
            // Don't want to add empty codes.
            String newItem = addCodeInput.getText();
            if (event.getCode() == KeyCode.ENTER && !newItem.equals("")) {
                account.setReadCodeBehavior(new ManualInputReader(addCodeInput));
                account.addCodes();
                codeListView.getItems().add(new CodeCell(newItem));
                addCodeInput.setText("");
            }
        });
    }

    /**
     * Clear the list of codes being displayed in this view
     * and only show the codes for a specific social media
     * account.
     *
     * @param name Name of the social media account to show
     *             the codes for.
     */
    public void addCodes(String name) {
        codeListView.getItems().clear();
        User user = Database.getUser();
        if (user != null) {
            this.account = user.getAccountByName(name);
            title.setText(account.getSocialMediaType() + "\n");
            usernameTitle.setText(name);

            // Only allow user to click the import codes button
            // if their account is supported.
            importCodes.setDisable(!SUPPORTED_PLATFORMS.contains(account.getSocialMediaType().toLowerCase()));

            List<String> codes = account.getUserCodes();
            for (String s : codes) {
                codeListView.getItems().add(new CodeCell(s));
            }
        }
    }

    /**
     * Return the Account that's currently
     * being used to display the codes.
     *
     * @return Account object which is currently
     * is in use.
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * Allows a user to logout and redirects them to the home page.
     */
    public void handleLogout() {
        Database.logUserOut();
        View.switchSceneTo(CodeView.getInstance(), HomePageView.getInstance());
    }

    /**
     * Switches the scene to the SettingsView once the settings button is clicked.
     */
    public void handleSettings() {
        ((Reversible) SettingsView.getInstance()).setPreviousView(CodeView.getInstance());
        View.switchSceneTo(CodeView.getInstance(), SettingsView.getInstance());
    }

    /**
     * Switches the scene to the Account-Viewer view once the back button is clicked.
     */
    public void handleGoBack() {
        View.switchSceneTo(CodeView.getInstance(), AccountView.getInstance());
    }
}
