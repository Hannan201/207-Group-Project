package cypher.enforcers;

import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Tests for using the user-data-related services.
 */
public class UserSavingTests {
    private static Injector injector;

    private static PasswordHasher hasher;

    @BeforeAll
    public static void create() {
        injector = new Injector();
        hasher = new PasswordHasher();
    }

    @AfterAll
    public static void tearDown() {
        injector = null;
        hasher = null;
    }

    @Test
    public void saveUserTest() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_create.db");

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

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, hasher)),
                "Could not inject Password Hashing service"
        );

        // No user should be logged in.
        Optional<User> optionalUser = userRepository.read();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(userRepository.create("Joe", "1234"));
        assertEquals(userRepository.getLoggedInUser(), 1, "Only one user should be present in database.");
        dbService.disconnect();
    }

    @Test
    public void saveUserThemeTest() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_create_2.db");

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

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, hasher)),
                "Could not inject Password Hashing service"
        );

        // No user should be logged in.
        Optional<User> optionalUser = userRepository.read();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(userRepository.create("Joe", "1234"));
        assertEquals(userRepository.getLoggedInUser(), 1, "Only one user should be present in database.");
        assertTrue(userRepository.update(Theme.DARK));
        dbService.disconnect();
    }

}
