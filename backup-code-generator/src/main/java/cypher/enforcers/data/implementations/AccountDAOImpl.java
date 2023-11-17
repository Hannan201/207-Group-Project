package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.entities.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 Implementation for the Account Data Access Object (DAO) to communicate to
 the database and make changes to any information related to the accounts.
 */
public class AccountDAOImpl implements AccountDAO {

    /** SQLite query to insert an account into the database. */
    private static final String ADD_ACCOUNT = "INSERT INTO accounts (user_id, name, type) VALUES (?, ?, ?)";

    /**
     * SQLite query to select the most recent account that was inserted
     * into the database.
     */
    private static final String GET_ACCOUNT_AFTER_ADDING = "SELECT * FROM accounts WHERE id = last_insert_rowid()";

    /** SQLite query to delete all accounts from the database. */
    private static final String DELETE_ACCOUNTS = "DELETE FROM accounts WHERE user_id = ?";

    /** SQLite query to delete an account by ID from the database. */
    private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id = ?";

    /** SQLite query to select all accounts by ID from the database. */
    private static final String GET_ACCOUNTS = "SELECT * FROM accounts WHERE user_id = ?";

    /** SQLite query to select an account by ID from the database. */
    private static final String GET_ACCOUNT = "SELECT * FROM accounts WHERE id = ?";

    /** Logger for the account data access object. */
    private static final Logger logger = LoggerFactory.getLogger(AccountDAOImpl.class);

    /** Service to communicate to the database. */
    private final DatabaseService databaseService;

    /**
     * Create a new Account Data Access Object to load accounts
     * from a database.
     *
     * @param service The service that provides a connection to the
     *                database.
     */
    public AccountDAOImpl(DatabaseService service) {
        this.databaseService = service;
    }

    /**
     * Get all accounts for a user.
     *
     * @param userID The ID of the user.
     * @return List of accounts. Returns null if no accounts
     * are found.
     */
    @Override
    public List<AccountEntity> getAccounts(long userID) {
        try {
            return databaseService.executeMultiSelect(GET_ACCOUNTS, AccountEntity.class, userID);
        } catch (SQLException e) {
            logger.debug("Failed select query. Cause: ", e);
        }

        return null;
    }

    /**
     * Get an account by ID.
     *
     * @param accountID ID of the account to retrieve.
     * @return Account if found, null otherwise.
     */
    @Override
    public AccountEntity getAccount(long accountID) {
        try {
            return databaseService.executeSelect(GET_ACCOUNT, AccountEntity.class, accountID);
        } catch (SQLException e) {
            logger.debug("Failed select query. Cause: ", e);
        }

        return null;
    }

    /**
     * Add an account.
     *
     * @param account Account to add.
     * @return Account if added, null otherwise.
     */
    @Override
    public AccountEntity addAccount(AccountEntity account) {
        try {
            databaseService.executeUpdate(ADD_ACCOUNT, account);

            return databaseService.executeSelect(GET_ACCOUNT_AFTER_ADDING, AccountEntity.class);
        } catch (SQLException e) {
            logger.debug("Failed insert query. Cause: ", e);
        }

        return null;
    }

    /**
     * Remove an account.
     *
     * @param id ID of the account to delete.
     * @return Account if deleted, null otherwise.
     */
    @Override
    public AccountEntity removeAccount(long id) {
        try {
            AccountEntity account = getAccount(id);

            if (account == null) {
                return null;
            }

            databaseService.executeUpdate(DELETE_ACCOUNT, id);
            return account;
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
        }

        return null;
    }

    /**
     * Remove all accounts for a user given the ID.
     *
     * @param userID ID of the user.
     * @return Accounts that we deleted, null otherwise.
     */
    @Override
    public List<AccountEntity> clearAllAccounts(long userID) {
        try {
            List<AccountEntity> accounts = getAccounts(userID);

            if (accounts == null) {
                return null;
            }

            databaseService.executeUpdate(DELETE_ACCOUNTS, userID);
            return accounts;
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
        }

        return null;
    }
}
