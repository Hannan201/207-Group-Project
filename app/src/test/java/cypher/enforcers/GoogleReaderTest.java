package cypher.enforcers;

import cypher.enforcers.code.readers.types.ReaderType;
import org.junit.jupiter.api.Test;

import cypher.enforcers.code.readers.CodeReaderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoogleReaderTest {

    //write your own tests with your partner!!

    @Test
    void googleReaderTest() {
        String filePath = Samples.GOOGLE.getPath();
        ArrayList<String> lst = new ArrayList<>(Objects.requireNonNull(CodeReaderFactory.makeCodeReader(ReaderType.GOOGLE)).extractCodes(filePath));
        String[] arr = new String[] {"18474856", "69420975", "74927467", "59309594", "67329797", "19586940", "58599843", "10569806", "22218604", "83928282"};
        ArrayList<String> GoogleSample = new ArrayList<>(List.of(arr));
        assertEquals(GoogleSample, lst);
    }
}