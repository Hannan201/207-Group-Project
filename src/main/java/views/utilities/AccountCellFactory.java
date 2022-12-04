package views.utilities;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import user.Account;
import views.utilities.AccountCell;

public class AccountCellFactory implements Callback<ListView<Account>, ListCell<Account>> {

    @Override
    public ListCell<Account> call(ListView<Account> param) {
        return new AccountCell();
    }
}