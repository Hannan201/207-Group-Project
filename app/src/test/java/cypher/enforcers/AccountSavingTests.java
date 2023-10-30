package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.spis.*;
import cypher.enforcers.models.Account;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSavingTests {

    // Username: Hannan
    // Password: 12345

    // Username: Joe
    // Password: 1234

    // Username: test
    // Password: hellobro

    @Test
    public void saveAccountTest() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_create.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        Account account = new Account();
        account.setName("One");
        account.setSocialMediaType("Reddit");
        account.setUserId(1);

        Optional<Account> optionalAccount = accountRepository.create(account);
        assertTrue(optionalAccount.isPresent(), "Returned account should not be empty.");

        dbService.disconnect();
    }

    @Test
    public void clearAllAccounts() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_clean.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        List<Account> accounts = accountRepository.deleteAll(1);
        assertFalse(accounts.isEmpty(), "Accounts were not deleted.");

        dbService.disconnect();
    }

    @Test
    public void deleteAccount() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_delete.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        Account one = new Account();
        one.setId(1);

        Optional<Account> account = accountRepository.delete(one.getID());
        assertTrue(account.isPresent(), "Account was not deleted.");

        Account three = new Account();
        three.setId(3);

        account = accountRepository.delete(three.getID());
        assertTrue(account.isPresent(), "Account was not deleted.");

        dbService.disconnect();
    }

}
