package code.readers;

import java.util.List;

/**
 * An abstract class to never be instated which is
 * responsible for reading backup codes from a text
 * file.
 */

public abstract class CodeReader {

    // File path for the text file.
    private String filePath;

    /**
     * Read in a text file and return the backup codes
     * in that text file.
     *
     * @param fileName Location to the text file.
     * @return List of backup codes.
     */
    public abstract List<String> extractCodes(String fileName);

    /**
     * Set the file path.
     * @param newPath The new file destination.
     */
    public void setFilePath(String newPath) {
        this.filePath = newPath;
    }

    /**
     * Return the file path for this reader.
     * @return File path for this reader.
     */
    public String getFilePath() {
        return this.filePath;
    }
}
