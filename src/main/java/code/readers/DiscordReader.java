package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for reading backup codes
 * in the structure provided by Discord.
 */

public class DiscordReader extends CodeReader implements ReadCodeBehavior {

    /**
     * Extract the backup codes from a text file in the
     * structure given by Discord.
     *
     * @param fileName Location to the text file.
     * @return The list of backup codes in the file.
     */
    @Override
    public List<String> extractCodes(String fileName) {

        List<String> codes = new ArrayList<>();
        try {
            Scanner read = new Scanner(new File(fileName));
            read.nextLine(); // skips the message in the .txt file
            read.nextLine(); // skips the blank line
            while (read.hasNextLine()) {
                String code = read.nextLine();
                code = code.substring(2, 6) + code.substring(7);
                codes.add(code.strip());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return codes;
    }

    /**
     * Read the backup codes from a text file in the
     * structure provided by Discord.
     *
     * @return The list of backup codes.
     */

   // Hannan if you are reading this, I have succeeded.

    @Override
    public List<String> readCodes() {
        return extractCodes(this.getFilePath());
    }
}
