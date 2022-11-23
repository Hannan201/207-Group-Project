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
        ArrayList<String> backupCodes = new ArrayList<>();
        File file = new File(fileName);

        try {
            FileReader readFile = new FileReader(file);
            BufferedReader in = new BufferedReader(readFile);
            String line;

            while ((line = in.readLine()) != null) {
                backupCodes.add(line.strip());
            }

            in.close();
            readFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
