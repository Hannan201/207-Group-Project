import code.readers.CodeReaderFactory;
import code.readers.DiscordReader;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DiscordReaderTest {

    @Test
    public void DiscordReaderTest() {

        DiscordReader dr = (DiscordReader) CodeReaderFactory.makeCodeReader("discord");
        List<String> actual_codes = dr.extractCodes("discord_codes_sample.txt");
        List<String> expected_codes = new ArrayList<String>(Arrays.asList("1a3har9a", "9atq1rav", "qxgth36p", "ytqrgctn", "rezbht67", "jetgqyih", "qhwfjrqb", "vzatd1h1", "uvert31e", "vg2qabcd"));
        assertEquals(expected_codes, actual_codes);

    }

}