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
import javafx.scene.layout.VBox;
import cypher.enforcers.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.AccountView;
import cypher.enforcers.views.CodeView;
import cypher.enforcers.views.View;

import java.io.IOException;

public class AccountCell extends ListCell<Account> {

    // Logger for the account cell.
    private static final Logger logger = LoggerFactory.getLogger(AccountView.class);

    @FXML
    private ImageView logo;

    @FXML
    private Label platformName;

    @FXML
    private Label username;

    @FXML
    private HBox cell;

    @FXML
    private VBox textBox;

    public AccountCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(Utilities.loadFileByURL("/cypher/enforcers/view/AccountCell.fxml"));
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            logger.error("Failed to load FXML file from resources: /cypher/enforcers/view/AccountCell.fxml. Cause: ", e);
        }
    }

    /**
     * Creates an AccountCell which represents an account visually
     * within the listView.
     *
     * @param item the account that is being passed in.
     * @param empty Whether it should add to the list view
     *              or not.
     */
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
                Image image = new Image(path);
                logo.setImage(image);

            // Adds a default logo for the account if it is not Discord, Google,
            // GitHub, or Shopify

            } else {
                path = Account.getIcons().get("default");
                Image image = new Image(path);
                logo.setImage(image);
            }

            logo.setFitWidth(50);
            logo.setFitHeight(50);
            cell.getChildren();
            HBox.setHgrow(cell, Priority.NEVER);
            setWrapText(true); // wraps the text in the ListCell to avoid long text

            platformName.setText(item.getSocialMediaType());
            platformName.setMaxWidth(200); // limits the amount of space that a username can take in the AccountCell
            username.setText(item.getName());
            platformName.setMaxWidth(200); // limits the amount of space that a platform can take in the AccountCell
            HBox.setHgrow(textBox, Priority.ALWAYS);
            cell.setSpacing(10);

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