package cypher.enforcers.models;

import cypher.enforcers.data.spis.AccountRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * This class is used to model an account in our application.
 */
public class AccountModel {

    // Used to interact with account objects.
    private AccountRepository accountRepository;

    // List of accounts for the current user.
    private final ObservableList<Account> accounts = FXCollections.observableArrayList();

    // Property to store the list of accounts.
    private final ObjectProperty<ObservableList<Account>> accountsProperty = new SimpleObjectProperty<>(accounts);

    /**
     * Get the accounts for the current user.
     *
     * @return An ObservableList of accounts.
     */
    public ObservableList<Account> getAccounts() {
        return accountsProperty.get();
    }

    /**
     * Get the list of accounts property.
     *
     * @return An ObservableList of accounts.
     */
    public ObjectProperty<ObservableList<Account>> accountsProperty() {
        return accountsProperty;
    }

    /**
     * Set the list of accounts.
     *
     * @param accounts The new accounts to be set as an
     *                 ObservableList.
     */
    public void setAccounts(ObservableList<Account> accounts) {
        accountsProperty.set(accounts);
    }

    // Property to store the current account being selected.
    private final ObjectProperty<Account> currentAccountProperty = new SimpleObjectProperty<>();

    /**
     * Get the current account being selected.
     *
     * @return The account being selected.
     */
    public Account getCurrentAccount() {
        return currentAccountProperty.get();
    }

    /**
     * Get the property of the current account.
     *
     * @return Property of the current account.
     */
    public ObjectProperty<Account> currentAccountProperty() {
        return currentAccountProperty;
    }

    /**
     * Set the current account being selected.
     *
     * @param account The Account to be set.
     */
    private void setCurrentAccount(Account account) {
        currentAccountProperty.set(account);
    }


    /**
     * Delete an account for this user.
     *
     * @param accountToDelete Account to delete.
     *
     * @return True if successfully deleted, false otherwise.
     */
    public boolean deleteAccounts(Account accountToDelete) {
        return accountRepository.delete(accountToDelete);
    }

    /**
     * Search for an account by name. The search is case-insensitive.
     *
     * @param name The name to search for.
     * @return An Optional containing an account when found, otherwise
     * null.
     */
    public Optional<Account> searchForAccount(String name) {
        return accountRepository.readByName(name);
    }

    /**
     * Add a new account.
     *
     * @param name Name of the account.
     * @param type Social Media type of the account.
     * @return True if the account was created, false otherwise.
     */
    public boolean addAccount(String name, String type) {
        Account account = new Account(-1, name, type);
        boolean result = accountRepository.create(account);
        if (result) {
            accounts.add(account);
        }

        return result;
    }
}
