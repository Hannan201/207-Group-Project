package cypher.enforcers.data.spis;

import cypher.enforcers.models.Account;

import java.util.List;
import java.util.Optional;

/*
 Interface for the Account Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public interface AccountRepository {

    /**
     * Create a new account.
     *
     * @param account Account to create.
     * @return ID of the account, otherwise -1.
     */
    boolean create(Account account);

    /**
     * Read all accounts.
     *
     * @return List of accounts. Empty list is returned if no accounts
     * are present.
     */
    List<Account> readAll();

    /**
     * Read an account by its ID.
     *
     * @param accountID ID of the account.
     * @return An Optional containing the account. Null otherwise.
     */
    Optional<Account> readByID(long accountID);

    /**
     * Read an account by name (case-insensitive).
     *
     * @param name Name of the account.
     * @return An Optional containing the account. Null otherwise.
     */
    Optional<Account> readByName(String name);

    /**
     * Delete an account.
     *
     * @param account Account to delete.
     * @return True if the account was deleted successfully, false otherwise.
     */
    boolean delete(Account account);

    /**
     * Delete all accounts.
     *
     * @return True if all accounts were deleted successfully, false
     * otherwise.
     */
    boolean deleteAll();
}
