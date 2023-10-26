package cypher.enforcers.data.spis;

import cypher.enforcers.models.Account;

import java.util.List;
import java.util.Optional;

/*
 Interface for the Account Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public interface AccountDAO {

    /**
     * Get all accounts for a user.
     *
     * @param userID The ID of the user.
     * @return List of accounts. Returns an empty list if no accounts
     * are found.
     */
    List<Account> getAccounts(long userID);

    /**
     * Get an account by ID.
     *
     * @param accountID ID of the account to retrieve.
     * @return An optional containing the account if found. Null otherwise.
     */
    Optional<Account> getAccount(long accountID);

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
    Optional<Account> getAccountByName(long userID, String name);

    /**
     * Add an account.
     *
     * @param account Account to add.
     * @return True if added, false otherwise.
     */
    boolean addAccount(Account account);

    /**
     * Remove an account.
     *
     * @param accountID ID of the account.
     * @return True if account was removed successfully, false otherwise.
     */
    boolean removeAccount(long accountID);

    /**
     * Remove all accounts for a user.
     *
     * @param userID ID of the user.
     * @return True if the accounts were removed successfully, false
     * otherwise.
     */
    boolean clearAllAccounts(long userID);
}
