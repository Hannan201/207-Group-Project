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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoadingTests {

    @Test
    public void loadsUsernames() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("user_read.db");

        UserDAO userDAO = new UserDAOImpl(dbService);
        UserRepository userRepository = new UserRepositoryImpl(userDAO);
        UserDTOMapper mapper = new UserDTOMapper();
        AuthenticationService authService = new AuthenticationServiceImpl(userRepository, mapper);

        assertTrue(authService.checkUsername("joe"), "Username is not taken.");
        assertTrue(authService.checkUsername("haNNan"), "Username is not taken.");
        assertFalse(authService.checkUsername("Goof no space gunk"), "Username is taken.");
        dbService.disconnect();
    }

    @Test
    public void loadLoggedInUser() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("user_read_2.db");

        UserDAO userDAO = new UserDAOImpl(dbService);

        UserRepository userRepository = new UserRepositoryImpl(userDAO);

        // One user should be logged in.
        Optional<UserEntity> optionalUser = userRepository.findLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");
        UserEntity user = optionalUser.get();

        assertEquals(user.getUsername(), "Joe", "Usernames don't match.");
        assertEquals(user.getID(), 1, "ID does not match.");
        assertTrue(user.getLoggedIn(), "User should be logged in.");
        assertEquals(user.getTheme(), Theme.DARK, "Themes do not match.");

        // Re-read the user to see if it gets the correct one the
        // second time.
        optionalUser = userRepository.findLoggedInUser();
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
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("user_read_3.db");

        UserDAO userDAO = new UserDAOImpl(dbService);
        UserRepository userRepository = new UserRepositoryImpl(userDAO);
        UserDTOMapper mapper = new UserDTOMapper();
        AuthenticationService authService = new AuthenticationServiceImpl(userRepository, mapper);

        assertTrue(authService.authenticateUser("hannan", "12345"), "Failed to login user.");

        Optional<UserEntity> optionalUser = userRepository.findLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");

        UserEntity user = optionalUser.get();
        assertEquals(user.getID(), 1, "Id for first user not equal.");
        assertTrue(user.getPassword().contains("WtlWQL"), "Password does not match.");

        dbService.disconnect();
    }

    @Test
    public void loadsDataForAuthenticationSecond() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("user_read_4.db");

        UserDAO userDAO = new UserDAOImpl(dbService);
        UserRepository userRepository = new UserRepositoryImpl(userDAO);
        UserDTOMapper mapper = new UserDTOMapper();
        AuthenticationService authService = new AuthenticationServiceImpl(userRepository, mapper);

        assertTrue(authService.authenticateUser("joe", "1234"), "Failed to login user.");

        Optional<UserEntity> optionalUser = userRepository.findLoggedInUser();
        assertTrue(optionalUser.isPresent(), "User should not be null.");

        UserEntity user = optionalUser.get();
        assertEquals(user.getID(), 2, "Id for first user not equal.");
        assertTrue(user.getPassword().contains("tTRgbJ"), "Password does not match.");
        dbService.disconnect();
    }

}
