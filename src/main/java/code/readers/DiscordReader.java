package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

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
        throw new UnsupportedOperationException();
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
