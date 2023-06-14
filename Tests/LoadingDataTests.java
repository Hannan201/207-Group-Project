import org.junit.jupiter.api.Test;
import models.Account;
import data.Database;
import models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadingDataTests {
    String pathToUserFile = "Tests/users.ser";

    String pathToConfigFile = "Tests/configurations.ser";


    @Test
    void userLoginLoaded() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        assertTrue(Database.checkUsername("haNNan"));
        assertTrue(Database.authenticateUser("Hannan", "12345"));
        assertTrue(Database.getLoginStatus());
        Database.logUserOut();
    }

    @Test
    void userAccountLoaded() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.authenticateUser("Hannan", "12345");
        User user = Database.getUser();
        assertNotNull(user);
        assertEquals(user.getUsername(), "Hannan");
        assertEquals(user.getAccounts().size(), 1);
        Account accounts = user.getAccounts().get(0);
        assertEquals(accounts.getName(), "Joe");
        assertEquals(accounts.getSocialMediaType(), "1234");
        assertEquals(accounts.getUserCodes().size(), 3);
        List<String> codes = accounts.getUserCodes();
        for (String s : codes) {
            assertEquals(s, "1234");
        }
    }
}