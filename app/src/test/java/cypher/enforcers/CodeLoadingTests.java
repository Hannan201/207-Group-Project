package cypher.enforcers;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.implementations.CodeDAOImpl;
import cypher.enforcers.data.implementations.CodeRepositoryImpl;
import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.data.spis.CodeRepository;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import cypher.enforcers.models.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class CodeLoadingTests {

    private static Injector injector;

    @BeforeAll
    public static void create() {
        injector = new Injector();
    }

    @AfterAll
    public static void destroy() {
        injector = null;
    }

    @Test
    public void readAllCodes() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_read_database.db");

        CodeDAO codeDAO = new CodeDAOImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeDAO, dbService)),
                "Could not inject database service."
        );

        CodeRepository codeRepository = new CodeRepositoryImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeRepository, codeDAO)),
                "Could not inject data access service."
        );

        Account account = new Account();
        account.setId(3);
        List<Code> codes = codeRepository.readAll(account);

        assertEquals(codes.size(), 16, "Number of codes does not match.");

        String[] expected = {
                "7294 8105",
                "4328 0505",
                "8829 8563",
                "5349 1328",
                "5804 5408",
                "3772 5051",
                "0084 5571",
                "9576 0658",
                "4231 7248",
                "8203 6638",
                "9286 1762",
                "6762 0652",
                "0886 4325",
                "0857 3901",
                "0609 7737",
                "1578 1639"
        };

        for (int i = 0; i < codes.size(); i++) {
            assertEquals(codes.get(i).getId(), i + 33, "ID of code " + (i + 1) + " should be " + (i + 33) + ".");
            assertEquals(codes.get(i).getCode(), expected[i], "Code " + (i + 1) + " should be " + expected[i]);
        }

        dbService.disconnect();
    }

    @Test
    public void readCodeByID() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_read_database.db");

        CodeDAO codeDAO = new CodeDAOImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeDAO, dbService)),
                "Could not inject database service."
        );

        CodeRepository codeRepository = new CodeRepositoryImpl();

        assertDoesNotThrow(
                () -> assertTrue(injector.injectServicesInto(codeRepository, codeDAO)),
                "Could not inject data access service."
        );

        dbService.disconnect();
    }

}
