import cypher.enforcers.code.readers.CodeReader;
import cypher.enforcers.code.readers.CodeReaderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GitHubReaderTests {

    @Test
    void readsValidGithubFile() {
        CodeReader reader = CodeReaderFactory.makeCodeReader("github");
        assertNotNull(reader);
        List<String> codes = reader.extractCodes("./Sample Text Files/github_codes_sample.txt");
        String[] expectedCodes = {"ggee1-d6a61",
                                  "814n1-8c8vh",
                                  "17eb1-74268",
                                  "rmvet-jrzb9",
                                  "za1gn-qvn18",
                                  "e175j-1h581",
                                  "b1j4c-17518",
                                  "0a7tg-a50h6",
                                  "1567a-1g146",
                                  "1d577-yt1f8",
                                  "055qs-6c0hh",
                                  "13dhc-65qa8",
                                  "2c6a1-6z183",
                                  "3ta7g-q48b1",
                                  "743qc-7641u",
                                  "3851e-467e1"};

        String[] codesAsArray = codes.toArray(new String[0]);
        assertArrayEquals(codesAsArray, expectedCodes);
    }

}
