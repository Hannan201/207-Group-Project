package cypher.enforcers.data.spis;

import cypher.enforcers.models.AccountEntity;

import java.util.List;

/*
 Interface for the Account Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public interface AccountDAO {

    /**
     * Get all accounts for a user.
     *
     * @param userID The ID of the user.
     * @return List of accounts. Returns null if no accounts
     * are found.
     */
    List<AccountEntity> getAccounts(long userID);

    /**
     * Get an account by ID.
     *
     * @param accountID ID of the account to retrieve.
     * @return Account if found, null otherwise.
     */
    AccountEntity getAccount(long accountID);

    /**
     * Add an account.
     *
     * @param account Account to add.
     * @return Account if added, null otherwise.
     */
    AccountEntity addAccount(AccountEntity account);

    /**
     * Remove an account.
     *
     * @param id ID of the account to delete.
     * @return Account if deleted, null otherwise.
     */
    AccountEntity removeAccount(long id);

    /**
     * Remove all accounts for a user given the ID.
     *
     * @param userID ID of the user.
     * @return Accounts that we deleted, null otherwise.
     */
    List<AccountEntity> clearAllAccounts(long userID);
}
