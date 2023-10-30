package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/*
 Implementation for the Account Data Access Object (DAO) to communicate to
 the database and make changes to any information related to the accounts.
 */
public class AccountDAOImpl implements AccountDAO {

    private static final String ADD_ACCOUNT = "INSERT INTO accounts (user_id, name, type) VALUES (?, ?, ?)";

    private static final String GET_ACCOUNT_AFTER_ADDING = "SELECT * FROM accounts WHERE id = last_insert_rowid()";

    private static final String DELETE_ACCOUNTS = "DELETE FROM accounts WHERE user_id = ?";

    private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id = ?";

    private static final String GET_ACCOUNTS = "SELECT * FROM accounts WHERE user_id = ?";

    private static final String GET_ACCOUNT = "SELECT * FROM accounts WHERE id = ?";

    // Logger for the account data access object.
    private static final Logger logger = LoggerFactory.getLogger(AccountDAOImpl.class);

    // Service to communicate to the database.
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
    public List<Account> getAccounts(long userID) {
        try {
            return databaseService.executeMultiSelect(GET_ACCOUNTS, Account.class, userID);
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
    public Account getAccount(long accountID) {
        try {
            return databaseService.executeSelect(GET_ACCOUNT, Account.class, accountID);
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
    public Account addAccount(Account account) {
        try {
            databaseService.executeUpdate(ADD_ACCOUNT, account);

            return databaseService.executeSelect(GET_ACCOUNT_AFTER_ADDING, Account.class);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }
    }

    /**
     * Remove an account.
     *
     * @param id ID of the account to delete.
     * @return Account if deleted, null otherwise.
     */
    @Override
    public Account removeAccount(long id) {
        try {
            Account account1 = getAccount(id);

            if (account1 == null) {
                return null;
            }

            databaseService.executeUpdate(DELETE_ACCOUNT, id);
            return account1;
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
            return null;
        }
    }

    /**
     * Remove all accounts for a user given the ID.
     *
     * @param userID ID of the user.
     * @return Accounts that we deleted, null otherwise.
     */
    @Override
    public List<Account> clearAllAccounts(long userID) {
        try {
            List<Account> accounts = getAccounts(userID);

            if (accounts == null) {
                return null;
            }

            databaseService.executeUpdate(DELETE_ACCOUNTS, userID);
            return accounts;
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
            return null;
        }
    }
}
