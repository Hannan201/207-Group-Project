package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

/**
 * This class is responsible for reading backup codes
 * in the structure provided by Google.
 */

public class GoogleReader extends CodeReader implements ReadCodeBehavior {

    /**
     * Extract the backup codes from a text file in the
     * structure given by Google.
     *
     * @param fileName Location to the text file.
     * @return The list of backup codes in the file.
     */
    @Override
    public List<String> extractCodes(String fileName) {
        throw new UnsupportedOperationException();
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
