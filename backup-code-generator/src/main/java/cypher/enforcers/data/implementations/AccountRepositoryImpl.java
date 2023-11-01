package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.data.entities.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*
 Implementation for the Account Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public class AccountRepositoryImpl implements AccountRepository {

    // Logger for the account repository.
    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    // To make updates to the database.
    private final AccountDAO accountDAO;

    /**
     * Create a new Account Repository linked to an Account Data Access
     * Object to work with the accounts as a collection.
     *
     * @param dao The Account Data Access Object.
     */
    public AccountRepositoryImpl(AccountDAO dao) {
        this.accountDAO = dao;
    }

    /**
     * Create a new account.
     *
     * @param account Account to create.
     * @return An Optional containing the account if created successfully,
     * null otherwise.
     */
    @Override
    public Optional<AccountEntity> create(AccountEntity account) {
        logger.trace("Attempting to create account with name {} and social media socialMediaType {}.", account.getName(), account.getSocialMediaType());

        AccountEntity createAccount = accountDAO.addAccount(account);

        if (!Objects.isNull(createAccount)) {
            logger.info("Account with name {} and social media type {} created.", createAccount.getName(), createAccount.getSocialMediaType());
            return Optional.of(createAccount);
        }

        logger.warn("Unable to create account with name {} and social media socialMediaType {}.", account.getName(), account.getSocialMediaType());
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
    public List<AccountEntity> readAll(long id) {
        logger.trace("Attempting to get all accounts for user with ID {}.", id);

        List<AccountEntity> result = accountDAO.getAccounts(id);

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
    public Optional<AccountEntity> read(long accountID) {
        logger.trace("Attempting to get account with ID {}.", accountID);

        AccountEntity account = accountDAO.getAccount(accountID);

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
     * @param id ID of the account to delete.
     * @return An Optional containing the account if successfully deleted,
     * null otherwise.
     */
    @Override
    public Optional<AccountEntity> delete(long id) {
        logger.trace("Attempting to delete account with ID {}.", id);

        AccountEntity deleteAccount = accountDAO.removeAccount(id);

        if (!Objects.isNull(deleteAccount)) {
            logger.info("Deleted account successfully.");
            return Optional.of(deleteAccount);
        }

        logger.warn("Failed to delete account with ID {}.", id);
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
    public List<AccountEntity> deleteAll(long id) {
        logger.trace("Attempting to delete all accounts for user with ID {}.", id);

        List<AccountEntity> accounts = accountDAO.clearAllAccounts(id);

        if (!Objects.isNull(accounts)) {
            logger.info("Deleted all accounts successfully.");
            return accounts;
        }

        logger.warn("Failed to delete all accounts for user with ID {}.", id);
        return Collections.emptyList();
    }
}
