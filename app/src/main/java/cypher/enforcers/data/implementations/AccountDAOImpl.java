package cypher.enforcers.data.implementations;

import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/*
 Implementation for the Account Data Access Object (DAO) to communicate to
 the database and make changes to any information related to the accounts.
 */
public class AccountDAOImpl implements AccountDAO {

    private static final String ADD_ACCOUNT = "INSERT INTO accounts (user_id, name, type) VALUES (?, ?, ?)";

    private static final String DELETE_ACCOUNTS = "DELETE FROM accounts WHERE user_id = ?";

    private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id = ?";

    private static final String GET_ACCOUNTS = "SELECT * FROM accounts WHERE user_id = ?";

    private static final String GET_ACCOUNT = "SELECT * FROM accounts WHERE id = ?";

    // Logger for the account data access object.
    private static final Logger logger = LoggerFactory.getLogger(AccountDAOImpl.class);

    // Service to communicate to the database.
    @SimpleService
    private DatabaseService databaseService;

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
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return account;
    }

    /**
     * Remove an account.
     *
     * @param account Account to delete.
     * @return Account if deleted, null otherwise.
     */
    @Override
    public Account removeAccount(Account account) {
        try {
            databaseService.executeUpdate(DELETE_ACCOUNT, account.getID());
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
            return null;
        }

        return account;
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
            databaseService.executeUpdate(DELETE_ACCOUNTS, userID);
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
            return null;
        }

        return List.of(new Account());
    }
}
