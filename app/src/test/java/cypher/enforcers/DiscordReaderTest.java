package cypher.enforcers;

import cypher.enforcers.code.readers.CodeReaderFactory;
import cypher.enforcers.code.readers.DiscordReader;

import cypher.enforcers.code.readers.types.ReaderType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DiscordReaderTest {

    @Test
    public void discordReaderTest() {
        DiscordReader dr = (DiscordReader) CodeReaderFactory.makeCodeReader(ReaderType.DISCORD);
        assert dr != null;
        List<String> actual_codes = dr.extractCodes(Samples.DISCORD.getPath());
        List<String> expected_codes = new ArrayList<>(Arrays.asList("1a3har9a", "9atq1rav", "qxgth36p", "ytqrgctn", "rezbht67", "jetgqyih", "qhwfjrqb", "vzatd1h1", "uvert31e", "vg2qabcd"));
        assertEquals(expected_codes, actual_codes);

    }

}