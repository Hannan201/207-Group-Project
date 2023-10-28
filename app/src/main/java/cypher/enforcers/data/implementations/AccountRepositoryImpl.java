package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.annotations.SimpleService;

import java.util.*;

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

    /**
     * Create a new account.
     *
     * @param account Account to create.
     * @return An Optional containing the account if created successfully,
     * null otherwise.
     */
    @Override
    public Optional<Account> create(Account account) {
        logger.trace("Attempting to create account with name {} and social media type {}.", account.getName(), account.getSocialMediaType());

        Account createAccount = accountDAO.addAccount(account);

        if (!Objects.isNull(createAccount)) {
            logger.trace("Account with name {} and social media type {} created with ID {}.", createAccount.getName(), createAccount.getSocialMediaType(), createAccount.getID());
            return Optional.of(createAccount);
        }

        logger.warn("Unable to create account with name {} and social media type {}.", account.getName(), account.getSocialMediaType());
        return Optional.empty();
    }

    /**
     * Read all accounts for a user given the ID.
     *
     * @param id ID of the user.
     * @return List of accounts. Empty list is returned if no accounts
     * are present.
     */
    @Override
    public List<Account> readAll(long id) {
        logger.trace("Attempting to get all accounts for user with ID {}.", id);

        List<Account> result = accountDAO.getAccounts(id);

        if (!Objects.isNull(result)) {
            logger.trace("Accounts retrieved for user with ID {}.", id);
            return result;
        }

        logger.trace("No accounts found for user with ID {}.", id);
        return Collections.emptyList();
    }

    /**
     * Read an account by its ID.
     *
     * @param accountID ID of the account.
     * @return An Optional containing the account. Null otherwise.
     */
    @Override
    public Optional<Account> read(long accountID) {
        logger.trace("Attempting to get account with ID {}.", accountID);

        Account account = accountDAO.getAccount(accountID);

        if (!Objects.isNull(account)) {
            logger.trace("Account found.");
            return Optional.of(account);
        }

        logger.warn("Failed to get account with ID {}.", accountID);
        return Optional.empty();
    }

    /**
     * Delete an account.
     *
     * @param account Account to delete.
     * @return An Optional containing the account if successfully deleted,
     * null otherwise.
     */
    @Override
    public Optional<Account> delete(Account account) {
        logger.trace("Attempting to delete account with ID {}.", account.getID());

        Account deleteAccount = accountDAO.removeAccount(account);

        if (!Objects.isNull(deleteAccount)) {
            logger.trace("Deleted account successfully.");
            return Optional.of(deleteAccount);
        }

        logger.warn("Failed to delete account with ID {}.", account.getID());
        return Optional.empty();
    }

    /**
     * Delete all accounts for a user given the user's ID.
     *
     * @param id ID of the user.
     * @return List containing the accounts if successfully deleted,
     * empty list otherwise.
     */
    @Override
    public List<Account> deleteAll(long id) {
        logger.trace("Attempting to delete all accounts for user with ID {}.", id);

        List<Account> accounts = accountDAO.clearAllAccounts(id);

        if (!Objects.isNull(accounts)) {
            logger.trace("Deleted all accounts successfully.");
            return accounts;
        }

        logger.warn("Failed to delete all accounts for user with ID {}.", id);
        return Collections.emptyList();
    }
}
