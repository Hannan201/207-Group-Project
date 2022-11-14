package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

/**
 * This class is responsible for reading backup codes
 * in the structure provided by Reddit.
 */

public class RedditReader extends CodeReader implements ReadCodeBehavior {

    /**
     * Extract the backup codes from a text file in the
     * structure given by Reddit.
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
     * structure provided by Reddit.
     *
     * @return The list of backup codes.
     */
    @Override
    public List<String> readCodes() {
        return extractCodes(this.getFilePath());
    }
}
