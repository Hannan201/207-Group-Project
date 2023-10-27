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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class CodeSavingAndLoadingTests {

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
    public void codeCreation() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_create_r.db");

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
        account.setId(2);
        List<Code> codes = codeRepository.readAll(account);
        assertEquals(codes.size(), 0, "Total number of codes doest not match.");

        Optional<Code> codeOptional = codeRepository.read(1);

        assertThrows(NoSuchElementException.class, codeOptional::get);

        Code previous = new Code();
        previous.setCode("123 456");
        previous.setAccountID(2);

        assertTrue(codeRepository.create(previous), "Code cannot be created.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/code_create_r.db");

        codes = codeRepository.readAll(account);
        assertEquals(codes.size(), 1, "Total number of codes doest not match.");

        assertEquals(codes.get(0).getId(), 1, "Code ID does not match.");
        assertEquals(codes.get(0).getCode(), "123 456", "Code does not match.");
        assertEquals(codes.get(0).getAccountID(), 2, "Account ID that this code belongs to does not match.");

        dbService.disconnect();
    }

    @Test
    public void codeDestruction() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/");

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

    @Test
    public void codeAlteration() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/");

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
