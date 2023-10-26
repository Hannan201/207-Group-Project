package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.data.spis.AuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 Implementation for the Account Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public class AccountRepositoryImpl implements AccountRepository {

    // Logger for the account repository.
    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    // To make updates to the database.
    @SimpleService
    private AccountDAO accountDAO;

    // Used to make sure user is authenticated.
    @SimpleService
    private AuthenticationService authService;

    /**
     * Create a new account.
     *
     * @param account Account to create.
     * @return True if the account was added, false otherwise.
     */
    @Override
    public boolean create(Account account) {
        logger.trace("Attempting to create account with name {} and social media type {}.", account.getName(), account.getSocialMediaType());

        boolean result = accountDAO.addAccount(account);

        if (result) {
            logger.trace("Account with name {} and social media type {} created with ID {}.", account.getName(), account.getSocialMediaType(), account.getID());
        } else {
            logger.warn("Unable to create account with name {} and social media type {}.", account.getName(), account.getSocialMediaType());
        }

        return result;
    }

    /**
     * Read all accounts.
     *
     * @return List of accounts. Empty list is returned if no accounts
     * are present.
     */
    @Override
    public List<Account> readAll() {
        long id = authService.getLoggedInUser();

        if (id == -1) {
            logger.warn("No use currently logged-in.");
            return new ArrayList<>();
        }

        logger.trace("Attempting to get all accounts for user with ID {}.", id);

        List<Account> result = accountDAO.getAccounts(id);

        if (result == null) {
            logger.trace("No accounts found for user with ID {}.", id);
            return new ArrayList<>();
        }

        logger.trace("Accounts retrieved for user with ID {}.", id);
        return result;
    }

    /**
     * Read an account by its ID.
     *
     * @param accountID ID of the account.
     * @return An Optional containing the account. Null otherwise.
     */
    @Override
    public Optional<Account> readByID(long accountID) {
        logger.trace("Attempting to get account with ID {}.", accountID);

        Optional<Account> account = accountDAO.getAccount(accountID);

        if (account.isPresent()) {
            logger.trace("Account found.");
        } else {
            logger.warn("Failed to get account with ID {}.", accountID);
        }

        return account;
    }

    /**
     * Read an account by name (case-sensitive).
     *
     * @param name Name of the account.
     * @return An Optional containing the account. Null otherwise.
     */
    @Override
    public Optional<Account> readByName(String name) {
        long id = authService.getLoggedInUser();

        if (id == -1) {
            logger.warn("No use currently logged-in");
            return Optional.empty();
        }

        logger.trace("Attempting to get account with name {} for user with ID {}.", name, id);

        Optional<Account> result = accountDAO.getAccountByName(id, name);

        if (result.isPresent()) {
            logger.trace("Account found.");
        } else {
            logger.warn("Failed to get account with name {} for user with ID {}.", name, id);
        }

        return result;
    }

    /**
     * Delete an account.
     *
     * @param account Account to delete.
     * @return True if the account was deleted successfully, false otherwise.
     */
    @Override
    public boolean delete(Account account) {
        logger.trace("Attempting to delete account with ID {}.", account.getID());

        boolean result = accountDAO.removeAccount(account.getID());

        if (result) {
            logger.trace("Deleted account successfully.");
        } else {
            logger.warn("Failed to delete account with ID {}.", account.getID());
        }

        return result;
    }

    /**
     * Delete all accounts.
     *
     * @return True if all accounts were deleted successfully, false
     * otherwise.
     */
    @Override
    public boolean deleteAll() {
        long id = authService.getLoggedInUser();

        if (id == -1) {
            logger.warn("No use currently logged-in");
            return false;
        }

        logger.trace("Attempting to delete all accounts for user with ID {}.", id);

        boolean result = accountDAO.clearAllAccounts(id);

        if (result) {
            logger.trace("Deleted all accounts successfully.");
        } else {
            logger.warn("Failed to delete all accounts for user with ID {}.", id);
        }

        return result;
    }
}
