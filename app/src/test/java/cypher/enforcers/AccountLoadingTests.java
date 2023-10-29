package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class AccountLoadingTests {

    private static Injector injector;

    @BeforeAll
    public static void create() {
        injector = new Injector();
    }

    @AfterAll
    public static void destroy() {
        injector = null;
    }

    @Test
    public void loadAllAccounts() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_read.db");

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

        UserDAOImpl userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject database service."
        );

        List<Account> accounts = accountRepository.readAll(1);

        assertEquals(accounts.size(), 3, "Accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "First account's ID does not match.");
        assertEquals(accounts.get(0).getName(), "Joe", "First account's name does not match.");
        assertEquals(accounts.get(0).getSocialMediaType(), "1234", "First account's social media type does not match.");
        assertEquals(accounts.get(0).getUserId(), 1, "First account's user ID does not match.");

        assertEquals(accounts.get(1).getID(), 3, "Second account's ID does not match.");
        assertEquals(accounts.get(1).getName(), "Razor", "Second account's name does not match.");
        assertEquals(accounts.get(1).getSocialMediaType(), "Origin", "Second account's social media type does not match.");
        assertEquals(accounts.get(1).getUserId(), 1, "Second account's user ID does not match.");

        assertEquals(accounts.get(2).getID(), 4, "Third account's ID does not match.");
        assertEquals(accounts.get(2).getName(), "ACOne", "Third account's name does not match.");
        assertEquals(accounts.get(2).getSocialMediaType(), "Discord", "Third account's social media type does not match.");
        assertEquals(accounts.get(2).getUserId(), 1, "Third account's user ID does not match.");

        dbService.disconnect();
    }

    @Test
    public void loadingWithID() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_read.db");

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

        Optional<Account> accountOptional = accountRepository.read(2);
        assertTrue(accountOptional.isPresent(), "First account is empty.");
        Account account = accountOptional.get();

        assertEquals(account.getID(), 2, "First account's ID does not match.");
        assertEquals(account.getName(), "Joe", "First account's name does not match.");
        assertEquals(account.getSocialMediaType(), "GitHub", "First account's social media type does not match.");
        assertEquals(account.getUserId(), 2, "First account's user ID does not match.");

        accountOptional = accountRepository.read(5);
        assertTrue(accountOptional.isPresent(), "Second account is empty.");
        account = accountOptional.get();

        assertEquals(account.getID(), 5, "Second account's ID does not match.");
        assertEquals(account.getName(), "ACTwo", "Second account's name does not match.");
        assertEquals(account.getSocialMediaType(), "Slack", "Second account's social media type does not match.");
        assertEquals(account.getUserId(), 2, "Second account's user ID does not match.");

        dbService.disconnect();
    }
}
