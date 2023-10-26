package cypher.enforcers.data.implementations;

import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.models.Account;

import java.util.List;
import java.util.Optional;

/*
 Implementation for the Account Data Access Object (DAO) to communicate to
 the database and make changes to any information related to the accounts.
 */
public class AccountDAOImpl implements AccountDAO {

    // Service to communicate to the database.
    @SimpleService
    private DatabaseService databaseService;

    /**
     * Get all accounts for a user.
     *
     * @param userID The ID of the user.
     * @return List of accounts. Returns an empty list if no accounts
     * are found.
     */
    @Override
    public List<Account> getAccounts(long userID) {
        System.out.println("Getting accounts for user");
        return null;
    }

    /**
     * Get an account by ID.
     *
     * @param accountID ID of the account to retrieve.
     * @return An optional containing the account if found. Null otherwise.
     */
    @Override
    public Optional<Account> getAccount(long accountID) {
        System.out.println("Getting account for user");
        return Optional.empty();
    }

    /**
     * Get an account, by name. The search is case-sensitive.
     * <br>
     * It's possible for two users to have the same account name, which
     * is why the user's ID is needed.
     *
     * @param userID ID of the user.
     * @param name Name of the account.
     * @return An optional if the account is found. Null otherwise.
     */
    @Override
    public Optional<Account> getAccountByName(long userID, String name) {
        System.out.println("Getting account for user with name");
        return Optional.empty();
    }

    /**
     * Add an account.
     *
     * @param account Account to add.
     * @return True if added, false otherwise.
     */
    @Override
    public boolean addAccount(Account account) {
        System.out.println("Adding account for user");
        return false;
    }

    /**
     * Remove an account.
     *
     * @param accountID ID of the account.
     * @return True if account was removed successfully, false otherwise.
     */
    @Override
    public boolean removeAccount(long accountID) {
        System.out.println("Removing account for user");
        return false;
    }

    /**
     * Remove all accounts for a user.
     *
     * @param userID ID of the user.
     * @return True if the accounts were removed successfully, false
     * otherwise.
     */
    @Override
    public boolean clearAllAccounts(long userID) {
        System.out.println("Removing all accounts for user");
        return false;
    }
}
