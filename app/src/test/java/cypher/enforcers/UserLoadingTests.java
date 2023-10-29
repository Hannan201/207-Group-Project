package cypher.enforcers;

import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoadingTests {
//
//    private static Injector injector;
//
//    @BeforeAll
//    public static void create() {
//        injector = new Injector();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        injector = null;
//    }
//
//    @Test
//    public void loadsUsernames() {
//        DatabaseService dbService = new SqliteHelper();
//        dbService.connect("/cypher/enforcers/user_read.db");
//
//        UserDAOImpl userDAO = new UserDAOImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
//                "Could not inject database service."
//        );
//
//        UserRepositoryImpl userRepository = new UserRepositoryImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
//                "Could not inject Data Access Service."
//        );
//
//        assertTrue(userRepository.checkUsername("joe"));
//        assertTrue(userRepository.checkUsername("haNNan"));
//        assertFalse(userRepository.checkUsername("Goof no space gunk"));
//        dbService.disconnect();
//    }
//
//    @Test
//    public void loadLoggedInUser() {
//        DatabaseService dbService = new SqliteHelper();
//        dbService.connect("/cypher/enforcers/user_read_2.db");
//
//        UserDAOImpl userDAO = new UserDAOImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
//                "Could not inject database service."
//        );
//
//        UserRepositoryImpl userRepository = new UserRepositoryImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
//                "Could not inject Data Access Service."
//        );
//
//        // One user should be logged in.
//        Optional<User> optionalUser = userRepository.read();
//        assertTrue(optionalUser.isPresent(), "User should not be null.");
//        User user = optionalUser.get();
//
//        assertEquals(user.getUsername(), "Joe", "Usernames don't match.");
//        assertEquals(user.getID(), 1, "ID does not match.");
//        assertTrue(user.getLoggedIn(), "User should be logged in.");
//        assertEquals(user.getTheme(), Theme.DARK, "Themes do not match.");
//
//        // Re-read the user to see if it gets the correct one the
//        // second time.
//        optionalUser = userRepository.read();
//        assertTrue(optionalUser.isPresent(), "User should not be null.");
//        user = optionalUser.get();
//
//        assertEquals(user.getUsername(), "Joe", "Usernames don't match.");
//        assertEquals(user.getID(), 1, "ID does not match.");
//        assertTrue(user.getLoggedIn(), "User should be logged in.");
//        assertEquals(user.getTheme(), Theme.DARK, "Themes do not match.");
//        dbService.disconnect();
//    }
//
//    @Test
//    public void loadLoggedInUserTheme() {
//        DatabaseService dbService = new SqliteHelper();
//        dbService.connect("/cypher/enforcers/user_read_2.db");
//
//        UserDAOImpl userDAO = new UserDAOImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
//                "Could not inject database service."
//        );
//
//        UserRepositoryImpl userRepository = new UserRepositoryImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
//                "Could not inject Data Access Service."
//        );
//
//        userRepository.read();
//
//        Optional<Theme> themeOptional = userRepository.readTheme();
//        assertTrue(themeOptional.isPresent(), "Theme should not be null.");
//        assertEquals(themeOptional.get(), Theme.DARK, "Theme should be dark mode.");
//
//        dbService.disconnect();
//    }
//
//    @Test
//    public void loadsDataForAuthentication() {
//        DatabaseService dbService = new SqliteHelper();
//        dbService.connect("/cypher/enforcers/user_read.db");
//
//        UserDAOImpl userDAO = new UserDAOImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userDAO, dbService)),
//                "Could not inject database service."
//        );
//
//        UserRepositoryImpl userRepository = new UserRepositoryImpl();
//        assertDoesNotThrow(
//                () -> assertTrue(injector.injectServicesInto(userRepository, userDAO)),
//                "Could not inject Data Access Service."
//        );
//
//        Map<String, String> data = userRepository.read("hannan");
//
//        assertNotNull(data);
//        assertEquals(data.keySet().size(), 2, "Should return two pairs.");
//        assertTrue(data.containsKey("password"), "Missing key: password.");
//        assertTrue(data.containsKey("id"), "Missing key: id.");
//
//        assertEquals(data.get("id"), "1", "Id for first user not equal.");
//        assertTrue(data.get("password").contains("RoQRUJ"));
//
//        data = userRepository.read("joe");
//
//        assertEquals(data.size(), 2, "Should return two pairs.");
//        assertTrue(data.containsKey("password"), "Missing key: password.");
//        assertTrue(data.containsKey("id"), "Missing key: id.");
//
//        assertTrue(data.get("password").contains("Oy+eiw"));
//        assertEquals(data.get("id"), "2", "Id for first user not equal.");
//
//        dbService.disconnect();
//    }

}
