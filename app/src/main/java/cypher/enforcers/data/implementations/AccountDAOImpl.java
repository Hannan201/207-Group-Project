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

    private static final String ADD_USER = "INSERT INTO accounts (user_id, name, type) VALUES (?, ?, ?) RETURNING id";

    private static final String DELETE_ACCOUNTS = "DELETE FROM accounts WHERE user_id = ?";

    private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id = ?";

    private static final String GET_ACCOUNTS = "SELECT * FROM accounts WHERE user_id = ?";

    // Logger for the account data access object.
    private static final Logger logger = LoggerFactory.getLogger(AccountDAOImpl.class);

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
        try {
            databaseService.executeUpdate(ADD_USER, account);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }

        return true;
    }

    /**
     * Remove an account.
     *
     * @param accountID ID of the account.
     * @return True if account was removed successfully, false otherwise.
     */
    @Override
    public boolean removeAccount(long accountID) {
        try {
            databaseService.executeUpdate(DELETE_ACCOUNT, accountID);
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
            return false;
        }

        return true;
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
        try {
            databaseService.executeUpdate(DELETE_ACCOUNTS, userID);
        } catch (SQLException e) {
            logger.debug("Failed delete query. Cause: ", e);
            return false;
        }

        return true;
    }
}
