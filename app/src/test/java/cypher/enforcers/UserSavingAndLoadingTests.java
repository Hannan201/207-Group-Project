package cypher.enforcers;

import cypher.enforcers.data.implementations.AuthenticationServiceImpl;
import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.security.PasswordHasher;
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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserSavingAndLoadingTests {

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
    public void loggingUserInAndOut() {
        // Username: test
        // Password: hellobro

        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_read_r.db");

        UserDAO userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject database service."
        );

        UserRepository userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Password Hashing service"
        );

        // No user should be logged in.
        Optional<User> optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.authenticateUser("test", "hellobro"), "User should be logged in.");

        optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        User user = optionalUser.get();

        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertEquals(user.getTheme(), Theme.LIGHT, "Themes do not match.");

        assertTrue(authService.logUserOut(user.getID()), "Unable to log user out.");

        optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        dbService.disconnect();
    }

    @Test
    public void testUserThemeUpdate() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_update_r.db");

        UserDAO userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject database service."
        );

        UserRepository userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        AuthenticationService authService = new AuthenticationServiceImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, userRepository)),
                "Could not inject Password Hashing service"
        );

        // No user should be logged in.
        Optional<User> optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.authenticateUser("test", "hellobro"), "User should be logged in.");

        optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        User user = optionalUser.get();

        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertEquals(user.getTheme(), Theme.LIGHT, "Themes do not match.");

        assertTrue(authService.updateUserTheme(user.getID(), Theme.HIGH_CONTRAST), "Update theme should be true.");

        assertTrue(authService.logUserOut(user.getID()), "Unable to log user out.");

        optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.authenticateUser("test", "hellobro"), "User should be logged in.");

        optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        user = optionalUser.get();

        assertEquals(user.getTheme(), Theme.HIGH_CONTRAST, "Themes do not match.");

        assertTrue(authService.logUserOut(user.getID()));

        optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        dbService.disconnect();
    }
}
