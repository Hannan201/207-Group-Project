package utilities;

import java.net.URL;
import java.util.Objects;

/*
 A class with utility methods to help load files from the resources
 folder.
 */
public class ResourceUtilities {

    /**
     * Load a file from the resource folder in the format of
     * a URL.
     *
     * @param path Path to the file relative to the resource folder.
     */
    public static URL loadFileByURL(String path) {
        return Objects.requireNonNull(
                ResourceUtilities.class.getClassLoader()
                        .getResource(path)
        );
    }

    /**
     * Return true if a string is an integer.
     *
     * @param s The string.
     * @return True if it is a integer, false otherwise.
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }

        return true;
    }

}
