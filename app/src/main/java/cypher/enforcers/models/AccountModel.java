package cypher.enforcers.models;

import cypher.enforcers.data.security.AccountDTO;
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
    private final ObservableList<AccountDTO> accounts = FXCollections.observableArrayList();

    // Property to store the list of accounts.
    private final ObjectProperty<ObservableList<AccountDTO>> accountsProperty = new SimpleObjectProperty<>(accounts);

    /**
     * Get the accounts for the current user.
     *
     * @return An ObservableList of accounts.
     */
    public ObservableList<AccountDTO> getAccounts() {
        return accountsProperty.get();
    }

    /**
     * Get the list of accounts property.
     *
     * @return Property with an ObservableList of accounts.
     */
    public ObjectProperty<ObservableList<AccountDTO>> accountsProperty() {
        return accountsProperty;
    }

    /**
     * Set the list of accounts.
     *
     * @param accounts The new accounts to be set as an
     *                 ObservableList.
     */
    public void setAccounts(ObservableList<AccountDTO> accounts) {
        accountsProperty.set(accounts);
    }

    // Property to store the current account being selected.
    private final ObjectProperty<AccountDTO> currentAccountProperty = new SimpleObjectProperty<>();

    /**
     * Get the current account being selected.
     *
     * @return The account being selected.
     */
    public AccountDTO getCurrentAccount() {
        return currentAccountProperty.get();
    }

    /**
     * Get the property of the current account.
     *
     * @return Property of the current account.
     */
    public ObjectProperty<AccountDTO> currentAccountProperty() {
        return currentAccountProperty;
    }

    /**
     * Set the current account being selected.
     *
     * @param account The Account to be set.
     */
    private void setCurrentAccount(AccountDTO account) {
        currentAccountProperty.set(account);
    }


    /**
     * Delete accounts for a user.
     *
     * @param id The ID of the user.
     * @param accountsToDelete Accounts to delete.
     * @return True if successfully deleted, false otherwise.
     */
    public boolean deleteAccounts(long id, AccountDTO ... accountsToDelete) {
        if (accountsToDelete.length == accounts.size()) {
            List<Account> results = accountRepository.deleteAll(id);

            if (results.size() == accountsToDelete.length) {
                accounts.clear();
                return true;
            }

            return false;
        }

        for (AccountDTO a : accountsToDelete) {
            Optional<Account> account = accountRepository.delete(a.id());
            if (account.isEmpty() || account.get().getID() != a.id()) {
                return false;
            }

            accounts.remove(a);
        }

        return true;
    }

    /**
     * Search for an account by name. The search is case-insensitive.
     *
     * @param name The name to search for.
     * @return An Optional containing an account when found, otherwise
     * null.
     */
    public Optional<AccountDTO> searchForAccount(String name) {
        List<AccountDTO> results = accounts.stream()
                .filter(a -> a.name().equals(name))
                .toList();

        if (results.size() == 1) {
            return Optional.of(results.get(0));
        }

        return Optional.empty();
    }

    /**
     * Add a new account.
     *
     * @param name Name of the account.
     * @param type Social Media type of the account.
     * @return True if the account was created, false otherwise.
     */
    public boolean addAccount(String name, String type) {
        Account account = new Account();
        account.setName(name);
        account.setSocialMediaType(type);
        Optional<Account> createdAccount = accountRepository.create(account);
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
     * @return True if accounts are deleted successfully, false otherwise.
     */
    public boolean clearAllAccounts(long id) {
        List<Account> results = accountRepository.deleteAll(id);
        if (results.size() == accounts.size()) {
            accounts.clear();
            return true;
        }

        return false;
    }
}
