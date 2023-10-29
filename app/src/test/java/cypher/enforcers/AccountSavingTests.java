package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.*;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.Account;
import cypher.enforcers.models.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSavingTests {

    private static Injector injector;

    @BeforeAll
    public static void create() {
        injector = new Injector();
    }

    @AfterAll
    public static void destroy() {
        injector = null;
    }

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

        AccountDAO accountDAO = new AccountDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Could not inject database service."
        );

        AccountRepository accountRepository = new AccountRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Could not inject Data Access Service."
        );

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

        AccountDAO accountDAO = new AccountDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Could not inject database service."
        );

        AccountRepository accountRepository = new AccountRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Could not inject Data Access Service."
        );

        List<Account> accounts = accountRepository.deleteAll(1);
        assertFalse(accounts.isEmpty(), "Accounts were not deleted.");

        dbService.disconnect();
    }

    @Test
    public void deleteAccount() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_delete.db");

        AccountDAO accountDAO = new AccountDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Could not inject database service."
        );

        AccountRepository accountRepository = new AccountRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Could not inject Data Access Service."
        );

        Account one = new Account();
        one.setId(1);

        Optional<Account> account = accountRepository.delete(one);
        assertTrue(account.isPresent(), "Account was not deleted.");

        Account three = new Account();
        three.setId(3);

        account = accountRepository.delete(three);
        assertTrue(account.isPresent(), "Account was not deleted.");

        dbService.disconnect();
    }

}
