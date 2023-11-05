package cypher.enforcers.views.accountview;

import cypher.enforcers.data.security.dtos.Account;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Factory to create an account cell.
 */
public class AccountCellFactory implements Callback<ListView<Account>, ListCell<Account>> {

    /**
     * Returns a new AccountCell when the user creates an account.
     *
     * @return a new AccountCell.
     * @throws UncheckedIOException If any errors occur when creating
     * the account cell.
     * @throws NullPointerException If any data is missing for when
     * creating this account cell.
     */
    @Override
    public ListCell<Account> call(ListView<Account> param) throws UncheckedIOException, NullPointerException {
        try {
            return new AccountCell();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}