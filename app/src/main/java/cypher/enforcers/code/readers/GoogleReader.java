package cypher.enforcers.code.readers;

import cypher.enforcers.behaviors.interfaces.ReadCodeBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for reading backup codes
 * in the structure provided by Google.
 */

public class GoogleReader extends CodeReader implements ReadCodeBehavior {

    // Logger for google reader.
    private static final Logger logger = LoggerFactory.getLogger(GoogleReader.class);

    /**
     * Extract the backup codes from a text file in the
     * structure given by Google.
     *
     * @param fileName Location to the text file.
     * @return The list of backup codes in the file.
     */
    @Override
    public List<String> extractCodes(String fileName) {
        logger.debug("Attempting to read codes from sourceL {}.", fileName);

        // read all the information
        ArrayList<String> info = new ArrayList<>();
        File file = new File(fileName);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                info.add(sc.nextLine());
            }
        } catch (IOException e) {
            logger.warn(String.format("Failed to read codes from %s. Cause: ", fileName), e);
            return new ArrayList<>();
        }


        // extract codes
        ArrayList<String> codes = new ArrayList<>();
        List<String> codesLine = new ArrayList<>(info.subList(3, 8));
        for (String line : codesLine) {
            codes.add(line.substring(3, 12).replace(" ",""));
            codes.add(line.substring(18, 27).replace(" ",""));
        }

        return codes;
    }

    /**
     * Read the backup codes from a text file in the
     * structure provided by Google.
     *
     * @return The List of backup codes.
     */
    @Override
    public List<String> readCodes() {
        return extractCodes(this.getFilePath());
    }
}
