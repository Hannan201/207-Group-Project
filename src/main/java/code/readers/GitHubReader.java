package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        // To store the codes.
        ArrayList<String> backupCodes = new ArrayList<>();

        try {
            // Make the objects needed to process the file.
            File file = new File(fileName);
            FileReader readFile = new FileReader(file);
            BufferedReader in = new BufferedReader(readFile);

            // To store each line of the file.
            String line;

            // GitHub's file structure is simple where
            // there is one column, and each row
            // contains a backup code.
            while ((line = in.readLine()) != null) {
                // To remove the new line character
                // and add it.
                backupCodes.add(line.strip());
            }

            // Close the streams.
            in.close();
            readFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the codes.
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
