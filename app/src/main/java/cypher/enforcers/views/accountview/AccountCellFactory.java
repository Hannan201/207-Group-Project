package cypher.enforcers.views.accountview;

import cypher.enforcers.data.security.dtos.Account;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Factory to create an account cell.
 */
public class AccountCellFactory implements Callback<ListView<Account>, ListCell<Account>> {

    /**
     * Returns a new AccountCell when the user creates an account.
     *
     * @return a new AccountCell.
     */
    @Override
    public ListCell<Account> call(ListView<Account> param) {
        return new AccountCell();
    }
}