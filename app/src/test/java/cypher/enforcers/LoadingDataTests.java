package cypher.enforcers;

import cypher.enforcers.data.security.Token;
import cypher.enforcers.code.Code;
import org.junit.jupiter.api.Test;
import cypher.enforcers.models.Account;
import cypher.enforcers.data.database.Database;
import cypher.enforcers.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadingDataTests {
    @Test
    void userLoginLoaded() {
        Database.setConnectionSource("/cypher/enforcers/database_with_two_users.db");

        Token token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        assertTrue(Database.checkUsername("haNNan"));

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void userAccountLoaded() {
        Database.setConnectionSource("/cypher/enforcers/database_with_two_users.db");

        Token token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);
        assertEquals(user.getUsername(), "hannan");

        List<Account> accounts = Database.getAccounts(token);
        assertEquals(accounts.size(), 1);
        Account account = accounts.get(0);
        assertEquals(account.getName(), "Joe");
        assertEquals(account.getSocialMediaType(), "1234");

        List<Code> codes = Database.getCodes(token, (int) account.getID());
        assertEquals(codes.size(), 3);
        for (Code c : codes) {
            assertEquals(c.getCode(), "1234");
        }

        Database.logUserOut(token);
        Database.disconnect();
    }
}