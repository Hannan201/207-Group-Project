package cypher.enforcers;

import cypher.enforcers.data.entities.CodeEntity;
import cypher.enforcers.data.implementations.CodeDAOImpl;
import cypher.enforcers.data.implementations.CodeRepositoryImpl;
import cypher.enforcers.data.implementations.SqliteHelper;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.data.spis.CodeRepository;
import cypher.enforcers.data.spis.DatabaseService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class CodeSavingAndLoadingTests {

    @Test
    public void codeCreation() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_create_r.db");

        CodeDAO codeDAO = new CodeDAOImpl(dbService);
        CodeRepository codeRepository = new CodeRepositoryImpl(codeDAO);

        List<CodeEntity> codes = codeRepository.readAll(2);
        assertEquals(codes.size(), 0, "Total number of codes doest not match.");

        Optional<CodeEntity> codeOptional = codeRepository.read(1);

        assertThrows(NoSuchElementException.class, codeOptional::get, "Code is not empty.");

        CodeEntity previous = new CodeEntity();
        previous.setCode("123 456");
        previous.setAccountID(2);

        Optional<CodeEntity> optionalCode = codeRepository.create(previous);
        assertTrue(optionalCode.isPresent(), "Code cannot be created.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/code_create_r.db");

        codes = codeRepository.readAll(2);
        assertEquals(codes.size(), 1, "Total number of codes doest not match.");

        assertEquals(codes.get(0).getId(), 1, "Code ID does not match.");
        assertEquals(codes.get(0).getCode(), "123 456", "Code does not match.");
        assertEquals(codes.get(0).getAccountID(), 2, "Account ID that this code belongs to does not match.");

        dbService.disconnect();
    }

    @Test
    public void codeDestruction() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_delete_r.db");

        CodeDAO codeDAO = new CodeDAOImpl(dbService);
        CodeRepository codeRepository = new CodeRepositoryImpl(codeDAO);

        Optional<CodeEntity> codeOptional = codeRepository.read(50);
        assertTrue(codeOptional.isPresent(), "Code is not present.");
        CodeEntity c = codeOptional.get();

        assertEquals(c.getId(), 50, "ID does not match.");
        assertEquals(c.getCode(), "123 456", "Code value does not match.");
        assertEquals(c.getAccountID(), 2, "Account ID that this code belongs to does not match.");

        Optional<CodeEntity> optionalCode = codeRepository.delete(c.getId());
        assertTrue(optionalCode.isPresent(), "Failed to delete code.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/code_delete_r.db");

        Optional<CodeEntity> code = codeRepository.read(50);

        assertThrows(NoSuchElementException.class, code::get, "Could is not empty.");

        dbService.disconnect();
    }

    @Test
    public void codeAlteration() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/code_update_r.db");

        CodeDAO codeDAO = new CodeDAOImpl(dbService);
        CodeRepository codeRepository = new CodeRepositoryImpl(codeDAO);

        Optional<CodeEntity> codeOptional = codeRepository.read(33);
        assertTrue(codeOptional.isPresent(), "Code is not present.");
        CodeEntity c = codeOptional.get();

        assertEquals(c.getId(), 33, "ID does not match.");
        assertEquals(c.getCode(), "7294 8105", "Code value does not match.");
        assertEquals(c.getAccountID(), 3, "Account ID that this code belongs to does not match.");

        c.setCode("ABCD EFGH");

        Optional<CodeEntity> optionalCode = codeRepository.update(c);
        assertTrue(optionalCode.isPresent(), "Failed to update code.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/code_update_r.db");

        codeOptional = codeRepository.read(33);
        assertTrue(codeOptional.isPresent(), "Code is not present.");
        c = codeOptional.get();

        assertEquals(c.getId(), 33, "ID does not match.");
        assertEquals(c.getCode(), "ABCD EFGH", "Code value does not match.");
        assertEquals(c.getAccountID(), 3, "Account ID that this code belongs to does not match.");

        dbService.disconnect();
    }

}
