package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for reading backup codes
 * in the structure provided by GitHub.
 */

public class GitHubReader extends CodeReader implements ReadCodeBehavior {

    /**
     * Extract the backup codes from a text file in the
     * structure given by GitHub.
     *
     * @param fileName Location to the text file.
     * @return The list of backup codes in the file.
     */
    @Override
    public List<String> extractCodes(String fileName) {
        ArrayList<String> backupCodes = new ArrayList<>();
        File file = new File(fileName);

        return backupCodes;
    }

    /**
     * Read the backup codes from a text file in the
     * structure provided by GitHub.
     *
     * @return The list of backup codes.
     */
    @Override
    public List<String> readCodes() {
        return extractCodes(this.getFilePath());
    }
}
