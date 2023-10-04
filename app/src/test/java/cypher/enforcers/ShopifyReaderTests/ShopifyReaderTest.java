package cypher.enforcers.ShopifyReaderTests;

import cypher.enforcers.code.readers.*;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class ShopifyReaderTest {

    // ------------------------------------- extractCodes() Tests -----------------------------------------------------

    @Test
    public void extractCodesBasic() {
        String filename = "../Sample Text Files/shopify_sample_codes.txt";
        ShopifyReader reader = new ShopifyReader();

        List<String> result = reader.extractCodes(filename);

        List<String> expected = new ArrayList<>();
        expected.add("92B5-4668-0AA7");
        expected.add("4CDA-650F-037C");
        expected.add("A233-4557-E539");
        expected.add("E146-19D5-C0BF");
        expected.add("0873-8F8D-7A25");
        expected.add("3FEF-4442-CD7F");
        expected.add("C2D6-B68F-97A3");
        expected.add("019B-DA4A-07ED");
        expected.add("E7D5-246F-9A9D");
        expected.add("91A2-C5EA-315B");

        assertEquals(expected, result);
    }












}
