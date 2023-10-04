package cypher.enforcers;

import cypher.enforcers.data.security.Token;
import org.junit.jupiter.api.Test;
import cypher.enforcers.data.database.Database;
import cypher.enforcers.models.User;

import static org.junit.jupiter.api.Assertions.*;

public class SavingDataTests {
    @Test
    void testSaveUserData() {
        Database.setConnectionSource("/cypher/enforcers/test.db");

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
        Database.setConnectionSource("/cypher/enforcers/test.db");

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
