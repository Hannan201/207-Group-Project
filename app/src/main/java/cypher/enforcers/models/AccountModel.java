package cypher.enforcers.models;

import cypher.enforcers.data.entities.AccountEntity;
import cypher.enforcers.data.security.Account;
import cypher.enforcers.data.security.AccountDTOMapper;
import cypher.enforcers.data.spis.AccountRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

/**
 * This class is used to model an account in our application.
 */
public class AccountModel {

    // Used to interact with account objects.
    private final AccountRepository accountRepository;

    private final AccountDTOMapper mapper;

    /**
     * Create a new account model linked to an Account Repository with
     * a mapper to convert an account object to a transfer object.
     *
     * @param repository The repository containing the account.
     * @param mapper The mapper that converts an account object to be
     *               transferred.
     */
    public AccountModel(AccountRepository repository, AccountDTOMapper mapper) {
        this.accountRepository = repository;
        this.mapper = mapper;
    }

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
     * @return Property with an ObservableList of accounts.
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
     * Load accounts for a user given the ID.
     *
     * @param id ID of the user.
     */
    public void loadAccounts(long id) {
        List<Account> converted = accountRepository.readAll(id)
                .stream().map(mapper)
                .toList();

        accounts.setAll(converted);
    }

    /**
     * Delete accounts for a user.
     *
     * @param id               The ID of the user.
     * @param accountsToDelete Accounts to delete.
     */
    public void deleteAccounts(long id, List<Account> accountsToDelete) {
        if (accountsToDelete.size() == accounts.size()) {
            List<AccountEntity> results = accountRepository.deleteAll(id);

            if (results.size() == accountsToDelete.size()) {
                accounts.clear();
            }

            return;
        }

        for (Account a : accountsToDelete) {
            Optional<AccountEntity> account = accountRepository.delete(a.id());
            if (account.isEmpty() || account.get().getID() != a.id()) {
                return;
            }

            accounts.remove(a);
        }
    }

    /**
     * Search for an account by name. The search is case-insensitive.
     *
     * @param name The name to search for.
     * @return An Optional containing an account when found, otherwise
     * null.
     */
    public Optional<Account> searchForAccount(String name) {
        List<Account> results = accounts.stream()
                .filter(a -> a.name().equals(name))
                .toList();

        if (results.size() == 1) {
            return Optional.of(results.get(0));
        }

        return Optional.empty();
    }

    /**
     * Add a new account for a user.
     *
     * @param id The ID of the user this account should belong to.
     * @param name Name of the account.
     * @param type Social Media type of the account.
     * @return True if the account was created, false otherwise.
     */
    public boolean addAccount(long id, String name, String type) {
        AccountEntity account = new AccountEntity();
        account.setName(name);
        account.setSocialMediaType(type);
        account.setUserId(id);
        Optional<AccountEntity> createdAccount = accountRepository.create(account);
        if (createdAccount.isPresent()) {
            accounts.add(mapper.apply(createdAccount.get()));
            return true;
        }

        return false;
    }

    /**
     * Clear all accounts for a user.
     *
     * @param id The ID of the user.
     */
    public void clearAllAccounts(long id) {
        List<AccountEntity> results = accountRepository.deleteAll(id);
        if (results.size() == accounts.size()) {
            accounts.clear();
        }
    }

    /**
     * Clear the current list of accounts. usually called when no
     * user is logged in and the account view is loaded.
     */
    public void clear() {
        accounts.clear();
    }
}
