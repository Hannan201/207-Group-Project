package cypher.enforcers;

import cypher.enforcers.data.implementations.AuthenticationServiceImpl;
import cypher.enforcers.data.implementations.SQLiteHelper;
import cypher.enforcers.data.implementations.UserDAOImpl;
import cypher.enforcers.data.implementations.UserRepositoryImpl;
import cypher.enforcers.data.security.mappers.UserDTOMapper;
import cypher.enforcers.data.spis.AuthenticationService;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.UserDAO;
import cypher.enforcers.data.spis.UserRepository;
import cypher.enforcers.data.entities.UserEntity;
import cypher.enforcers.views.themes.Theme;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserSavingAndLoadingTests {

    @Test
    public void loggingUserInAndOut() {
        // Username: test
        // Password: hellobro

        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("/cypher/enforcers/user_read_r.db");

        UserDAO userDAO = new UserDAOImpl(dbService);
        UserRepository userRepository = new UserRepositoryImpl(userDAO);
        UserDTOMapper mapper = new UserDTOMapper();
        AuthenticationService authService = new AuthenticationServiceImpl(userRepository, mapper);

        // No user should be logged in.
        Optional<UserEntity> optionalUser = userRepository.findLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.authenticateUser("test", "hellobro"), "User should be logged in.");

        optionalUser = userRepository.findLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        UserEntity user = optionalUser.get();

        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertEquals(user.getTheme(), Theme.LIGHT, "Themes do not match.");

        assertTrue(authService.logUserOut(user.getID()), "Unable to log user out.");

        optionalUser = userRepository.findLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        dbService.disconnect();
    }

    @Test
    public void testUserThemeUpdate() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("/cypher/enforcers/user_update_r.db");

        UserDAO userDAO = new UserDAOImpl(dbService);
        UserRepository userRepository = new UserRepositoryImpl(userDAO);
        UserDTOMapper mapper = new UserDTOMapper();
        AuthenticationService authService = new AuthenticationServiceImpl(userRepository, mapper);

        // No user should be logged in.
        Optional<UserEntity> optionalUser = userRepository.findLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.authenticateUser("test", "hellobro"), "User should be logged in.");

        optionalUser = userRepository.findLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        UserEntity user = optionalUser.get();

        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getUsername(), "test", "Username does not match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertEquals(user.getTheme(), Theme.LIGHT, "Themes do not match.");

        assertTrue(authService.updateUserTheme(user.getID(), Theme.HIGH_CONTRAST), "Update theme should be true.");

        assertTrue(authService.logUserOut(user.getID()), "Unable to log user out.");

        optionalUser = userRepository.findLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        assertTrue(authService.authenticateUser("test", "hellobro"), "User should be logged in.");

        optionalUser = userRepository.findLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        user = optionalUser.get();

        assertEquals(user.getTheme(), Theme.HIGH_CONTRAST, "Themes do not match.");

        assertTrue(authService.logUserOut(user.getID()));

        optionalUser = userRepository.findLoggedInUser();
        assertThrows(
                NoSuchElementException.class,
                optionalUser::get
        );

        dbService.disconnect();
    }
}
