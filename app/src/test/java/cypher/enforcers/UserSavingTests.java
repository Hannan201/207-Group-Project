package cypher.enforcers;

import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.security.PasswordHasher;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Tests for using the user-data-related services.
 */
public class UserSavingTests {

    private static UserRepositoryImpl userRepository;

    private static Injector injector;

    @BeforeAll
    public static void injectServices() {
        injector = new Injector();

        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/empty_database.db");

        UserDAOImpl userDAO = new UserDAOImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
                "Could not inject database service."
        );


        userRepository = new UserRepositoryImpl();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
                "Could not inject Data Access Service."
        );

        PasswordHasher hash = new PasswordHasher();
        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(userRepository, hash)),
                "Could not inject Password Hashing service"
        );
    }

    @Test
    public void saveUserTest() {
        // No user should be logged in.
        Optional<User> optionalUser = userRepository.read();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(userRepository.create("Joe", "1234"));
        assertEquals(userRepository.getLoggedInUser(), 1, "Only one user should be present in database.");
    }

}
