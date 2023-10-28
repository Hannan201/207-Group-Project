package cypher.enforcers;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.*;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.Account;
import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to simulate application interaction such as signing in, signing out,
 * signing up, adding an account, deleting an account, adding a code,
 * deleting a code, and updating a code.
 */
public class ApplicationIntegrationTests {

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
    public void interactionOne() {
        /*
        Here's the interaction:
        - User signs up
        - Creates two accounts:
            First account:
                    Name: bruh
                    Type: Reddit
            Second account:
                    Name: moment
                    Type: Google
        - Adds two codes for the first account
        - Adds one code for the second account
        - sign out
        - sign back in and verify the data is still there.
         */

        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/interaction_1.db");

        UserDAOImpl userDAO = new UserDAOImpl();
        AccountDAO accountDAO = new AccountDAOImpl();
        CodeDAO codeDAO = new CodeDAOImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Cannot inject Database service into UserDAO."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Cannot inject Database service into AccountDAO."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeDAO, dbService)),
                "Cannot inject Database service into CodeDAO."
        );

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        AccountRepository accountRepository = new AccountRepositoryImpl();
        CodeRepository codeRepository = new CodeRepositoryImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Cannot inject Data Access Service service into User Repository."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Cannot inject Data Access Service service into Account Repository."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeRepository, codeDAO)),
                "Cannot inject Data Access Service service into Code Repository."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        PasswordHasher passwordHash = new PasswordHasher();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Cannot inject Repository into Authentication service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, passwordHash)),
                "Cannot inject Password Hashing service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, authService)),
                "Cannot inject Password Hashing service."
        );

        Optional<User> userOptional = userRepository.read();
        assertThrows(NoSuchElementException.class, userOptional::get, "User is not empty.");

        assertTrue(userRepository.create("Test", "password"), "User cannot register.");

        userOptional = userRepository.read();
        assertTrue(userOptional.isPresent(), "User is empty.");
        User user = userOptional.get();

        assertEquals(user.getID(), 1, "User ID does not match.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertFalse(user.getPassword().isEmpty(), "Password is empty.");
        assertEquals(user.getTheme(), Theme.LIGHT, "Theme does not match.");
        assertTrue(user.getLoggedIn(), "User is not logged in.");

        Account one = new Account();
        one.setName("bruh");
        one.setSocialMediaType("Reddit");
        one.setUserId(user.getID());
        assertTrue(accountRepository.create(one), "Cannot create first account.");

        Account two = new Account();
        two.setName("moment");
        two.setSocialMediaType("Google");
        two.setUserId(user.getID());
        assertTrue(accountRepository.create(two), "Cannot create second account.");

        Code codeOne = new Code();
        codeOne.setCode("111");
        codeOne.setAccountID(one.getID());
        assertTrue(codeRepository.create(codeOne), "Cannot create first code for first account.");

        Code codeTwo = new Code();
        codeTwo.setCode("222");
        codeTwo.setAccountID(one.getID());
        assertTrue(codeRepository.create(codeTwo), "Cannot create second code for first account.");

        Code codeThree = new Code();
        codeThree.setCode("333");
        codeThree.setAccountID(two.getID());
        assertTrue(codeRepository.create(codeThree), "Cannot create first code for second account.");

        assertTrue(authService.logUserOut(), "Cannot log user out.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/interaction_1.db");

        assertTrue(authService.authenticateUser("Test", "password"), "User cannot log in.");

        userOptional = userRepository.read();
        assertTrue(userOptional.isPresent(), "User is empty.");
        user = userOptional.get();

        assertEquals(user.getID(), 1, "User ID does not match.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertFalse(user.getPassword().isEmpty(), "Password is empty.");
        assertEquals(user.getTheme(), Theme.LIGHT, "Theme does not match.");
        assertTrue(user.getLoggedIn(), "User is not logged in.");

        List<Account> accounts = accountRepository.readAll();

        assertEquals(accounts.size(), 2, "Number of accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "First account ID does not match.");
        assertEquals(accounts.get(0).getName(), "bruh", "First account name does not match.");
        assertEquals(accounts.get(0).getSocialMediaType(), "Reddit", "First account type does not match,");
        assertEquals(accounts.get(0).getUserId(), user.getID(), "User ID of the first account does not match.");

        assertEquals(accounts.get(1).getID(), 2, "Second account ID does not match.");
        assertEquals(accounts.get(1).getName(), "moment", "Second account name does not match.");
        assertEquals(accounts.get(1).getSocialMediaType(), "Google", "Second account type does not match,");
        assertEquals(accounts.get(1).getUserId(), user.getID(), "User ID of the second account does not match.");

        List<Code> codes = codeRepository.readAll(accounts.get(0));

        assertEquals(codes.size(), 2, "Number of codes for first account do not match.");

        assertEquals(codes.get(0).getId(), 1, "ID of first code does not match.");
        assertEquals(codes.get(0).getCode(), "111", "Code of first code does not match.");
        assertEquals(codes.get(0).getAccountID(), accounts.get(0).getID(), "Account ID of first code does not match.");

        assertEquals(codes.get(1).getId(), 2, "ID of second code does not match.");
        assertEquals(codes.get(1).getCode(), "222", "Code of second code does not match.");
        assertEquals(codes.get(1).getAccountID(), accounts.get(0).getID(), "Account ID of second code does not match.");

        codes = codeRepository.readAll(accounts.get(1));

        assertEquals(codes.size(), 1, "Number of codes for second account do not match.");

        assertEquals(codes.get(0).getId(), 3, "ID of third code does not match.");
        assertEquals(codes.get(0).getCode(), "333", "Code of third code does not match.");
        assertEquals(codes.get(0).getAccountID(), accounts.get(1).getID(), "Account ID of third code does not match.");

        assertTrue(authService.logUserOut(), "Cannot log user out.");

        dbService.disconnect();
    }

    @Test
    public void interactionTwo() {
         /*
            Here's the interaction:
            - User from the first interaction signs in
            - Deletes one account:
                Account that is deleted:
                        Name: moment
                        Type: Google
            - Deletes one code for the only account
            - Updates the remaining code
            - Changes the theme to dark mode
            - sign out
            - sign back in and verify the data is still there.
            - close application without logging out
         */

        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/interaction_2.db");

        UserDAOImpl userDAO = new UserDAOImpl();
        AccountDAO accountDAO = new AccountDAOImpl();
        CodeDAO codeDAO = new CodeDAOImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Cannot inject Database service into UserDAO."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Cannot inject Database service into AccountDAO."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeDAO, dbService)),
                "Cannot inject Database service into CodeDAO."
        );

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        AccountRepository accountRepository = new AccountRepositoryImpl();
        CodeRepository codeRepository = new CodeRepositoryImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Cannot inject Data Access Service service into User Repository."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Cannot inject Data Access Service service into Account Repository."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeRepository, codeDAO)),
                "Cannot inject Data Access Service service into Code Repository."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        PasswordHasher passwordHash = new PasswordHasher();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Cannot inject Repository into Authentication service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, passwordHash)),
                "Cannot inject Password Hashing service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, authService)),
                "Cannot inject Password Hashing service."
        );

        Optional<User> userOptional = userRepository.read();
        assertThrows(NoSuchElementException.class, userOptional::get, "User is not empty.");

        assertTrue(authService.authenticateUser("Test", "password"), "User cannot register.");

        userOptional = userRepository.read();
        assertTrue(userOptional.isPresent(), "User is empty.");
        User user = userOptional.get();

        assertTrue(userRepository.update(Theme.DARK), "Unable to update theme.");

        Optional<Account> accountOptional = accountRepository.readByID(2);
        assertTrue(accountOptional.isPresent(), "Account is empty.");
        Account account = accountOptional.get();

        assertEquals(account.getID(), 2, "Account ID does not match.");
        assertEquals(account.getName(), "moment", "Account name does not match.");
        assertEquals(account.getSocialMediaType(), "Google", "Account type does not match,");
        assertEquals(account.getUserId(), user.getID(), "User ID of the account does not match.");

        assertTrue(accountRepository.delete(account), "Unable to delete account.");

        accountOptional = accountRepository.readByID(1);
        assertTrue(accountOptional.isPresent(), "Second account is empty.");
        account = accountOptional.get();

        List<Code> codes = codeRepository.readAll(account);

        assertEquals(codes.size(), 2, "Number of codes does not match.");

        assertEquals(codes.get(0).getId(), 1, "ID of code does not match.");
        assertEquals(codes.get(0).getCode(), "111", "Code value of code does not match.");
        assertEquals(codes.get(0).getAccountID(), account.getID(), "Account ID of code does not match.");

        assertTrue(codeRepository.delete(codes.get(0)), "Unable to delete first code.");

        codes.get(1).setCode("420 530 640");

        assertTrue(codeRepository.update(codes.get(1)), "Unable to update second code.");

        assertTrue(authService.logUserOut(), "User cannot log out.");
        dbService.disconnect();
        dbService.connect("/cypher/enforcers/interaction_2.db");

        assertTrue(authService.authenticateUser("Test", "password"), "Unable to login user.");

        userOptional = userRepository.read();
        assertTrue(userOptional.isPresent(), "User is empty.");
        user = userOptional.get();

        assertEquals(user.getID(), 1, "User ID does not match.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertFalse(user.getPassword().isEmpty(), "Password is empty.");
        assertEquals(user.getTheme(), Theme.DARK, "Theme does not match.");
        assertTrue(user.getLoggedIn(), "User is not logged in.");

        List<Account> accounts = accountRepository.readAll();
        assertEquals(accounts.size(), 1,  "Account size (after reload) does not match.");
        account = accounts.get(0);

        assertEquals(account.getID(), 1, "Account ID (after reload) does not match.");
        assertEquals(account.getName(), "bruh", "Account name (after reload) does not match.");
        assertEquals(account.getSocialMediaType(), "Reddit", "Account type (after reload) does not match,");
        assertEquals(account.getUserId(), user.getID(), "User ID of the account (after reload) does not match.");

        codes = codeRepository.readAll(account);
        assertEquals(codes.size(), 1, "List of codes (after reload) does not match.");

        assertEquals(codes.get(0).getId(), 2, "ID of code (after reload) does not match.");
        assertEquals(codes.get(0).getCode(), "420 530 640", "Code value of code (after reload) does not match.");
        assertEquals(codes.get(0).getAccountID(), account.getID(), "Account ID (after reload) of code does not match.");

        dbService.disconnect();
    }

    @Test
    public void interactionThree() {
        /*
        Here's the interaction:
        - User from the second interaction starts the application
        - Verify the data is there since the user did not log out
        - log out
         */
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/interaction_3.db");

        UserDAOImpl userDAO = new UserDAOImpl();
        AccountDAO accountDAO = new AccountDAOImpl();
        CodeDAO codeDAO = new CodeDAOImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Cannot inject Database service into UserDAO."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountDAO, dbService)),
                "Cannot inject Database service into AccountDAO."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeDAO, dbService)),
                "Cannot inject Database service into CodeDAO."
        );

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        AccountRepository accountRepository = new AccountRepositoryImpl();
        CodeRepository codeRepository = new CodeRepositoryImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Cannot inject Data Access Service service into User Repository."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, accountDAO)),
                "Cannot inject Data Access Service service into Account Repository."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeRepository, codeDAO)),
                "Cannot inject Data Access Service service into Code Repository."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        PasswordHasher passwordHash = new PasswordHasher();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Cannot inject Repository into Authentication service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, passwordHash)),
                "Cannot inject Password Hashing service."
        );

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(accountRepository, authService)),
                "Cannot inject Password Hashing service."
        );

        Optional<User> userOptional = userRepository.read();
        assertTrue(userOptional.isPresent(), "User is empty.");
        User user = userOptional.get();

        assertEquals(user.getID(), 1, "User ID does not match.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertFalse(user.getPassword().isEmpty(), "Password is empty.");
        assertEquals(user.getTheme(), Theme.DARK, "Theme does not match.");
        assertTrue(user.getLoggedIn(), "User is not logged in.");

        List<Account> accounts = accountRepository.readAll();
        assertEquals(accounts.size(), 1,  "Account size does not match.");
        Account account = accounts.get(0);

        assertEquals(account.getID(), 1, "Account ID does not match.");
        assertEquals(account.getName(), "bruh", "Account name does not match.");
        assertEquals(account.getSocialMediaType(), "Reddit", "Account type does not match,");
        assertEquals(account.getUserId(), user.getID(), "User ID of the account does not match.");

        List<Code> codes = codeRepository.readAll(account);
        assertEquals(codes.size(), 1, "List of codes does not match.");

        assertEquals(codes.get(0).getId(), 2, "ID of code does not match.");
        assertEquals(codes.get(0).getCode(), "420 530 640", "Code value of code does not match.");
        assertEquals(codes.get(0).getAccountID(), account.getID(), "Account ID of code does not match.");

        assertTrue(authService.logUserOut(), "Unable to log user out.");
        dbService.disconnect();
    }
}