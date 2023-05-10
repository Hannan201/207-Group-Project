package views.utilities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.util.Callback;
import user.Account;

public class AccountCellFactory implements Callback<ListView<Account>, ListCell<Account>> {

    private DoubleProperty baseSize;
    private ObjectProperty<Font> baseFontSize;

    public void setDefaultSizes(DoubleProperty newBaseSize, ObjectProperty<Font> newBaseFontSize) {
        this.baseSize = newBaseSize;
        this.baseFontSize = newBaseFontSize;
    }

    /**
     * Returns a new AccountCell when the user creates an account.
     *
     * @return a new AccountCell.
     */
    @Override
    public ListCell<Account> call(ListView<Account> param) {
        return new AccountCell(this.baseSize, this.baseFontSize);
    }
}