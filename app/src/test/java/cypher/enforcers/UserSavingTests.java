package cypher.enforcers;

import cypher.enforcers.data.implementations.AuthenticationServiceImpl;
import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.security.UserDTO;
import cypher.enforcers.data.security.UserDTOMapper;
import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import cypher.enforcers.data.spis.UserRepository;
import cypher.enforcers.injectors.Injector;
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

    @BeforeAll
    public static void create() {
        injector = new Injector();
    }

    @AfterAll
    public static void tearDown() {
        injector = null;
    }

    @Test
    public void saveUserTest() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_create.db");

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

        UserDTOMapper mapper = new UserDTOMapper();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, mapper)),
                "Could not inject Mapper service."
        );

        // No user should be logged in.
        Optional<UserDTO> optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.createUser("Joe", "1234"), "Unable to create user.");

        optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User is empty.");
        assertEquals(optionalUser.get().id(), 1, "Only one user should be present in database.");
        dbService.disconnect();
    }

    @Test
    public void saveUserThemeTest() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/user_create_2.db");

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

        UserDTOMapper mapper = new UserDTOMapper();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(authService, mapper)),
                "Could not inject Mapper service."
        );

        // No user should be logged in.
        Optional<UserDTO> optionalUser = authService.getLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.createUser("Joe", "1234"), "Unable to create user.");

        optionalUser = authService.getLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User is empty.");
        assertEquals(optionalUser.get().id(), 1, "Only one user should be present in database.");

        assertTrue(authService.updateUserTheme(optionalUser.get().id(), Theme.DARK), "Unable to update theme.");
        dbService.disconnect();
    }

}
