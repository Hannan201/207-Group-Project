package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        // read all the information
        ArrayList<String> info = new ArrayList<>();
        File file = new File(fileName);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                info.add(sc.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
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
