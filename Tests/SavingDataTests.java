import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Account;
import user.Database;
import user.User;

import static org.junit.jupiter.api.Assertions.*;

public class SavingDataTests {
    String pathToUserFile = "Tests/users.ser";
    String pathToConfigFile = "Tests/configurations.ser";

    @Test
    void testSaveUserData() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.registerUser("Joe", "1234");
        User newUser = Database.getUser();
        assertNotNull(newUser);
        Account a1 = new Account("Joe", "GitHub");
        a1.addCodes("1234");
        a1.addCodes("1234");
        a1.addCodes("1234");
        a1.addCodes("1234");
        newUser.addNewAccount(a1);
        Database.logUserOut();
    }

    @Test
    void testSaveUserLogins() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.registerUser("Hannan", "12345");
        User user = Database.getUser();
        assertNotNull(user);
        Account account = new Account("Joe", "1234");
        account.addCodes("1234");
        account.addCodes("1234");
        account.addCodes("1234");
        user.addNewAccount(account);
        Database.logUserOut();
    }
}
