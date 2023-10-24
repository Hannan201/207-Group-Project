package cypher.enforcers.views.utilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import cypher.enforcers.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.AccountView;
import cypher.enforcers.views.CodeView;
import cypher.enforcers.views.View;

import java.io.IOException;

/**
 * Class to represent a account cell.
 */
public class AccountCell extends ListCell<Account> {

    // Logger for the account cell.
    private static final Logger logger = LoggerFactory.getLogger(AccountCell.class);

    // Contains the contents of this cell.
    @FXML
    private HBox cell;

    // Holds the image for the social media type.
    @FXML
    private ImageView logo;

    // Label for the social media type.
    @FXML
    private Label platformName;

    // Label for the name of the account.
    @FXML
    private Label username;

    /**
     * Create a new account cell.
     */
    public AccountCell() {
        try {
            loadFXML();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the FXML file for the account cell.
     *
     * @throws IOException If the file could not be loaded correctly.
     */
    private void loadFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/cypher/enforcers/view/AccountCell.fxml"));

        /*
        The previous line of:

                loader.setController(this);

        worked just fine. The only reason I added this here was so that
        IntelliJ IDEA would stop showing warnings. This is to avoid
        JavaFX from creating a new instance and to instead use the current
        instance for the controller.
         */
        loader.setControllerFactory(param -> {
            if (param == AccountCell.class) {
                return this;
            }

            throw new RuntimeException("Could not find correct controller class for the AccountCell.");
        });

        loader.setRoot(this);
        loader.load();
    }

    @Override
    protected void updateItem(Account item, boolean empty) {
        super.updateItem(item, empty);

        // deals with the case of the account being null or the boolean being empty

        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {

            // checks if the account's platform is either Google, Discord, Shopify, or GitHub
            // and if so, adds a custom icon to the account
            String path;
            if (Account.supportsImport(item.getSocialMediaType().toLowerCase())) {
                path = Account.getIcons().get(item.getSocialMediaType().toLowerCase());
            } else {
                // Adds a default logo for the account if it is not Discord, Google,
                // GitHub, or Shopify
                path = Account.getIcons().get("default");
            }

            Image image = new Image(path);
            logo.setImage(image);

            cell.getChildren();
            HBox.setHgrow(cell, Priority.NEVER);
            setWrapText(true); // wraps the text in the ListCell to avoid long text

            platformName.setText(item.getSocialMediaType());
            username.setText(item.getName());

            // If the AccountCell is double-clicked, then this handle method will transition
            // the view from the AccountsView to the CodeView

            setOnMouseClicked(mouseClickedEvent -> {
                if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 2) {
                    logger.trace("Switching from the AccountView to the CodeView.");
                    View.switchSceneTo(AccountView.getInstance(), CodeView.getInstance());

                    ((CodeView) CodeView.getInstance()).getCodeViewController().addCodes(item.getID());
                }
            });

            setText(null);
            setGraphic(cell);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // displays the AccountCell accordingly
        }
    }
}