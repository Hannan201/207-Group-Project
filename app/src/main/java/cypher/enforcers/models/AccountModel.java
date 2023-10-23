package cypher.enforcers.models;

import cypher.enforcers.data.spis.AccountRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to model an account in our application.
 */
public class AccountModel {

    private AccountRepository accountRepository;

    private final ObservableList<Account> accounts = FXCollections.observableArrayList();

    private final ObjectProperty<ObservableList<Account>> accountsProperty = new SimpleObjectProperty<>(accounts);

    public ObservableList<Account> getAccounts() {
        return accountsProperty.get();
    }

    public ObjectProperty<ObservableList<Account>> accountsProperty() {
        return accountsProperty;
    }

    public void setAccounts(ObservableList<Account> accounts) {
        accountsProperty.set(accounts);
    }

    private final ObjectProperty<Account> currentAccountProperty = new SimpleObjectProperty<>();

    public Account getCurrentAccount() {
        return currentAccountProperty.get();
    }

    public ObjectProperty<Account> currentAccountProperty() {
        return currentAccountProperty;
    }

    private void setCurrentAccount(Account account) {
        currentAccountProperty.set(account);
    }


    public boolean deleteAccounts(ObservableList<Account> accountsToDelete) {
        if (accountsToDelete.size() == accounts.size()) {
            if (accountRepository.deleteAll()) {
                accounts.clear();
                return true;
            } else {
                return false;
            }
        }

        for (Account account : accountsToDelete) {
            if (accountRepository.delete(account)) {
                accounts.remove(account);
            } else {
                return false;
            }
        }

        return true;
    }
}
