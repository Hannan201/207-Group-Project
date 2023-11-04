package cypher.enforcers;

import cypher.enforcers.data.entities.CodeEntity;
import cypher.enforcers.data.implementations.CodeDAOImpl;
import cypher.enforcers.data.implementations.CodeRepositoryImpl;
import cypher.enforcers.data.implementations.SQLiteHelper;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.data.spis.CodeRepository;
import cypher.enforcers.data.spis.DatabaseService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class CodeLoadingTests {

    @Test
    public void readAllCodes() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("/cypher/enforcers/code_read.db");

        CodeDAO codeDAO = new CodeDAOImpl(dbService);
        CodeRepository codeRepository = new CodeRepositoryImpl(codeDAO);

        List<CodeEntity> codes = codeRepository.readAll(3);

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
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("/cypher/enforcers/code_read.db");

        CodeDAO codeDAO = new CodeDAOImpl(dbService);
        CodeRepository codeRepository = new CodeRepositoryImpl(codeDAO);

        Optional<CodeEntity> codeOptional = codeRepository.read(49);
        assertTrue(codeOptional.isPresent(), "Code is null.");
        CodeEntity c = codeOptional.get();

        assertEquals(c.getId(), 49, "ID should be 49.");
        assertEquals(c.getCode(), "EEE EEE", "Codes do not match.");
        assertEquals(c.getAccountID(), 4, "Code does not belong to correct account.");

        dbService.disconnect();
    }

}
