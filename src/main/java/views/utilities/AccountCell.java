package views.utilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import user.Account;
import views.AccountView;
import views.CodeView;

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

    @FXML
    private VBox textbox;

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

            if (Account.getIcons().containsKey(item.getSocialMediaType().toLowerCase())) {
                String path = Account.getIcons().get(item.getSocialMediaType().toLowerCase());
                Image image = new Image(path);
                logo.setImage(image);
                logo.setFitWidth(50);
                logo.setFitHeight(50);
                cell.getChildren();
                HBox.setHgrow(cell, Priority.NEVER);
            } else if (! Account.getIcons().containsKey(item.getSocialMediaType().toLowerCase())) {
                String path = Account.class.getClassLoader().getResource("images/icons8-app-100.png").toExternalForm();;
                Image image = new Image(path);
                logo.setImage(image);
                logo.setFitWidth(50);
                logo.setFitHeight(50);
                cell.getChildren();
                HBox.setHgrow(cell, Priority.NEVER);
            }
            setWrapText(true);

            platformName.setText(item.getSocialMediaType());
            platformName.setMaxWidth(200); // removes the horizontal scroll bar issue, should be added to username too
            username.setText(item.getName());
            HBox.setHgrow(textbox, Priority.ALWAYS);
            cell.setSpacing(10);

            setOnMouseClicked(mouseClickedEvent -> {
                if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 2) {
                    Scene scene = AccountView.getInstance().getRoot().getScene();
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(CodeView.getInstance().getCurrentThemePath());
                    scene.setRoot(CodeView.getInstance().getRoot());
                }
            });

            setText(null);
            setGraphic(cell);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}