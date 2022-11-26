import org.junit.jupiter.api.Test;


import behaviors.interfaces.ReadCodeBehavior;
import code.readers.CodeReaderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
public class googleReaderTesting {

    //write your own tests with your partner!!

    @Test
    void GoogleReaderTest() {
        String filePath = "Sample Text Files/google_codes_sample.txt";
        ArrayList<String> lst = new ArrayList<>(CodeReaderFactory.makeCodeReader("google").extractCodes(filePath));
        String[] arr = new String[] {"18474856", "69420975", "74927467", "59309594", "67329797", "19586940", "58599843", "10569806", "22218604", "83928282"};
        ArrayList<String> GoogleSample = new ArrayList<>(List.of(arr));
        assertEquals(GoogleSample, lst);



    }




}
