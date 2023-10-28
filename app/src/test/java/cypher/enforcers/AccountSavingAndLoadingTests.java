package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSavingAndLoadingTests {

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
    public void accountCreation() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_create_r.db");

        AccountDAOImpl accountDAO = new AccountDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Could not inject database service."
        );

        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Could not inject Data Access Service."
        );

        UserDAOImpl userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject database service."
        );

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        PasswordHasher hasher = new PasswordHasher();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, hasher)),
                "Could not inject Password hashing service."
        );

        AuthenticationServiceImpl authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Repository service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, authService)),
                "Could not inject Authentication service."
        );

        assertTrue(authService.authenticateUser("Hannan", "12345"), "Unable to log user in.");

        List<Account> accounts = accountRepository.readAll();

        assertEquals(accounts.size(), 1, "Total accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "ID should be 1.");
        assertEquals(accounts.get(0).getName(), "Joe", "Name should be Joe.");
        assertEquals(accounts.get(0).getSocialMediaType(), "1234", "Type should be 1234.");
        assertEquals(accounts.get(0).getUserId(), 1, "User ID should be 1.");

        Account account = new Account();
        account.setName("Apple Boy99");
        account.setSocialMediaType("Google");
        account.setUserId(1);
        assertTrue(accountRepository.create(account), "Account cannot be created.");

        assertTrue(authService.logUserOut(), "Unable to log user out.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/account_create_r.db");

        assertTrue(authService.authenticateUser("Hannan", "12345"));

        accounts = accountRepository.readAll();

        assertEquals(accounts.size(), 2, "Total accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "ID should be 1.");
        assertEquals(accounts.get(0).getName(), "Joe", "Name should be Joe.");
        assertEquals(accounts.get(0).getSocialMediaType(), "1234", "Type should be 1234.");
        assertEquals(accounts.get(0).getUserId(), 1, "User ID should be 1.");

        assertEquals(accounts.get(1).getID(), 3, "ID should be 3.");
        assertEquals(accounts.get(1).getName(), "Apple Boy99", "Name should be Apple Boy99.");
        assertEquals(accounts.get(1).getSocialMediaType(), "Google", "Type should be Google.");
        assertEquals(accounts.get(1).getUserId(), 1, "User ID should be 1.");

        assertTrue(authService.logUserOut(), "Cannot log user out.");
        dbService.disconnect();
    }

    @Test
    public void accountDestruction() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_delete_r.db");

        AccountDAOImpl accountDAO = new AccountDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Could not inject database service."
        );

        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Could not inject Data Access Service."
        );

        UserDAOImpl userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject database service."
        );

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        PasswordHasher hasher = new PasswordHasher();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, hasher)),
                "Could not inject Password hashing service."
        );

        AuthenticationServiceImpl authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Repository service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, authService)),
                "Could not inject Authentication service."
        );

        assertTrue(authService.authenticateUser("Joe", "1234"), "User can not log in.");

        List<Account> accounts = accountRepository.readAll();

        assertEquals(accounts.size(), 1, "Total accounts do not match.");

        assertEquals(accounts.get(0).getID(), 2, "ID should be 2.");
        assertEquals(accounts.get(0).getName(), "Joe", "Name should be Joe.");
        assertEquals(accounts.get(0).getSocialMediaType(), "GitHub", "Type should be GitHub.");
        assertEquals(accounts.get(0).getUserId(), 2, "User ID should be 2.");

        Account delete = accounts.get(0);

        assertTrue(accountRepository.delete(delete), "Unable to delete account.");

        assertTrue(authService.logUserOut(), "Unable to log user out.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/account_delete_r.db");

        assertTrue(authService.authenticateUser("Joe", "1234"));

        accounts = accountRepository.readAll();

        assertEquals(accounts.size(), 0, "Total accounts do not match.");

        assertTrue(authService.logUserOut(), "Unable to log user out.");

        dbService.disconnect();
    }

}
