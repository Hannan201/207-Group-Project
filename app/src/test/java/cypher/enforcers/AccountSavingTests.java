package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.Account;
import cypher.enforcers.models.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

        assertTrue(authService.authenticateUser("Hannan", "12345"), "User can not log in.");

        Optional<User> userOptional = userRepository.read();
        assertTrue(userOptional.isPresent(), "User not present.");
        User user = userOptional.get();

        Account account = new Account();
        account.setName("One");
        account.setSocialMediaType("Reddit");
        account.setUserId(user.getID());

        assertTrue(accountRepository.create(account), "User cannot create account.");

        dbService.disconnect();
    }

    @Test
    public void clearAllAccounts() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_clean.db");

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

        assertTrue(authService.authenticateUser("Hannan", "12345"), "User can not log in.");

        assertTrue(accountRepository.deleteAll(), "Accounts were not deleted.");

        dbService.disconnect();
    }

    @Test
    public void deleteAccount() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_delete.db");

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

        assertTrue(authService.authenticateUser("Test", "hellobro"), "User can not log in.");

        Account one = new Account();
        one.setId(1);

        assertTrue(accountRepository.delete(one), "Account was not deleted.");

        Account three = new Account();
        three.setId(3);

        assertTrue(accountRepository.delete(three), "Account was not deleted.");

        dbService.disconnect();
    }

}
