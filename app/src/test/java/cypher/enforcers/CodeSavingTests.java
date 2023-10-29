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
import java.util.Optional;

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
        dbService.connect("/cypher/enforcers/code_create.db");

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

        Optional<Code> optionalCode = codeRepository.create(c);
        assertTrue(optionalCode.isPresent(), "User cannot create code.");

        Code code = optionalCode.get();
        assertEquals(code.getId(), 1, "Code ID should be 1.");

        dbService.disconnect();
    }

    @Test
    public void updateCode() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_update.db");

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

        Code code = new Code();
        code.setCode("4EW C0D3");
        code.setId(33);

        Optional<Code> optionalCode = codeRepository.update(code);
        assertTrue(optionalCode.isPresent(), "User cannot update code.");

        dbService.disconnect();
    }

    @Test
    public void removeCode() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_delete.db");

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

        Code code = new Code();
        code.setId(1);

        Optional<Code> optionalCode = codeRepository.delete(code);
        assertTrue(optionalCode.isPresent(), "User cannot delete code.");

        dbService.disconnect();
    }

    @Test
    public void removeAllCodes() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_clean.db");

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

        List<Code> codes = codeRepository.deleteAll(account);
        assertFalse(codes.isEmpty(), "User cannot delete code.");

        dbService.disconnect();
    }

}
