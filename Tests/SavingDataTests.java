import data.Storage;
import data.Token;
import org.junit.jupiter.api.Test;
import models.Account;
import data.Database;
import models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SavingDataTests {
    @Test
    void testSaveUserData() {
        Path path = Path.of("./Tests/test.db");
        if (Files.exists(path)) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.registerUser("Joe", "1234");
        User user = Database.getUser(token);
        assertNotNull(user);
        int id = Database.addAccount(token, "Joe", "GitHub");
        int codeID = Database.addCode(token, id, "1234");
        codeID = Database.addCode(token, id, "1234");
        codeID = Database.addCode(token, id, "1234");
        codeID = Database.addCode(token, id, "1234");

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testSaveUserLogins() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.registerUser("Hannan", "12345");
        User user = Database.getUser(token);
        assertNotNull(user);
        int id = Database.addAccount(token, "Joe", "1234");
        int codeID = Database.addCode(token, id, "1234");
        codeID = Database.addCode(token, id, "1234");
        codeID = Database.addCode(token, id, "1234");
        Database.logUserOut(token);

        Database.disconnect();
    }
}
