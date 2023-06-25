package utilities;

import java.io.InputStream;
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

}
