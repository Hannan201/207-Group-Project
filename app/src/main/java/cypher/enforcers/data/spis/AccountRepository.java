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
     * @return An Optional containing the account if created successfully,
     * null otherwise.
     */
    Optional<Account> create(Account account);

    /**
     * Read all accounts for a user given the ID.
     *
     * @param id ID of the user.
     * @return List of accounts. Empty list is returned if no accounts
     * are present.
     */
    List<Account> readAll(long id);

    /**
     * Read an account by its ID.
     *
     * @param accountID ID of the account.
     * @return An Optional containing the account. Null otherwise.
     */
    Optional<Account> read(long accountID);

    /**
     * Delete an account.
     *
     * @param account Account to delete.
     * @return An Optional containing the account if successfully deleted,
     * null otherwise.
     */
    Optional<Account> delete(Account account);

    /**
     * Delete all accounts for a user given the user's ID.
     *
     * @param id ID of the user.
     * @return List containing the accounts if successfully deleted,
     * empty list otherwise.
     */
    List<Account> deleteAll(long id);
}
