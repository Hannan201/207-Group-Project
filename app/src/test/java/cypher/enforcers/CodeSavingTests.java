package cypher.enforcers;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.implementations.CodeDAOImpl;
import cypher.enforcers.data.implementations.CodeRepositoryImpl;
import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.data.spis.CodeRepository;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.injectors.Injector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CodeSavingTests {

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
    public void createCode() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_create_database.db");

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

        Code c = new Code();
        c.setCode("123 456");
        c.setAccountID(1);

        assertTrue(codeRepository.create(c), "User cannot create code.");
        assertEquals(c.getId(), 1, "Code ID should be 1.");

        dbService.disconnect();
    }

}
