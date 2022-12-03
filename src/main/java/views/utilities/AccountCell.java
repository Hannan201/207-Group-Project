package views.utilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import user.Account;

import java.io.IOException;

public class AccountCell extends ListCell<Account> {

    @FXML
    private ImageView logo;

    @FXML
    private Label platformName;

    @FXML
    private Label username;

    @FXML
    private HBox cell;

    public AccountCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/AccountCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Account item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {

            if (Account.getIcons().containsKey(item.getSocialMediaType())) {
                Image image = new Image("images/icons8-discord-100.png");
                logo.setImage(image);
                logo.setFitHeight(5);
                logo.setFitWidth(5);
            }
            platformName.setText(item.getSocialMediaType());
            username.setText(item.getName());
            setText(null);
            setGraphic(cell);

//            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}