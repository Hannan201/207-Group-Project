package cypher.enforcers;

import cypher.enforcers.data.implementations.AuthenticationServiceImpl;
import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import cypher.enforcers.data.spis.UserRepository;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoadingTests {

    private static Injector injector;

    @BeforeAll
    public static void create() {
        injector = new Injector();
    }

    @AfterAll
    public static void tearDown() {
        injector = null;
    }

    @Test
    public void loadsUsernames() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_read.db");

        UserDAO userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject Database service."
        );

        UserRepository userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Repository service."
        );

        assertTrue(authService.checkUsername("joe"), "Username is not taken.");
        assertTrue(authService.checkUsername("haNNan"), "Username is not taken.");
        assertFalse(authService.checkUsername("Goof no space gunk"), "Username is taken.");
        dbService.disconnect();
    }

    @Test
    public void loadLoggedInUser() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_read_2.db");

        UserDAO userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject Database service."
        );

        UserRepository userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Repository service."
        );

        // One user should be logged in.
        Optional<User> optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        User user = optionalUser.get();

        assertEquals(user.getUsername(), "Joe", "Usernames don't match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getTheme(), Theme.DARK, "Themes do not match.");

        // Re-read the user to see if it gets the correct one the
        // second time.
        optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        user = optionalUser.get();

        assertEquals(user.getUsername(), "Joe", "Usernames don't match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getTheme(), Theme.DARK, "Themes do not match.");
        dbService.disconnect();
    }

    @Test
    public void loadsDataForAuthentication() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_read_3.db");

        UserDAO userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject Database service."
        );

        UserRepository userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Repository service."
        );

        assertTrue(authService.authenticateUser("hannan", "12345"), "Failed to login user.");

        Optional<User> optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");

        User user = optionalUser.get();
        assertEquals(user.getID(), 1, "Id for first user not equal.");
        assertTrue(user.getPassword().contains("RoQRUJ"), "Password does not match.");

        dbService.disconnect();
    }

    @Test
    public void loadsDataForAuthenticationSecond() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_read_4.db");

        UserDAO userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject Database service."
        );

        UserRepository userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Repository service."
        );

        assertTrue(authService.authenticateUser("joe", "1234"), "Failed to login user.");

        Optional<User> optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");

        User user = optionalUser.get();
        assertEquals(user.getID(), 2, "Id for first user not equal.");
        assertTrue(user.getPassword().contains("Oy+eiw"), "Password does not match.");
        dbService.disconnect();
    }

}
