package cypher.enforcers;

import cypher.enforcers.utilities.Utilities;
import cypher.enforcers.views.themes.Theme;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Paths;

/*
 This classes contains the paths to the sample text files based on the
 social media platform. Mainly here for easy testing.
 */
public enum Samples {

    // Path to platforms.
    DISCORD("discord"),
    GITHUB("github"),
    GOOGLE("google"),
    SHOPIFY("shopify");

    // Holds the path to the file.
    private String path;

    Samples(String name) {
        try {
            this.path = Paths.get(
                    Utilities.loadFileByURL(
                            "/cypher/enforcers/Sample Text Files/" + name + "_codes_sample.txt"
                    ).toURI()
            ).toString();
        } catch (URISyntaxException e) {
            Logger logger = LoggerFactory.getLogger(Theme.class);
            logger.error(e, () ->
                    "Failed to load file from test resources /cypher/enforcers/Sample Text Files/" +
                    name +
                    "_codes_sample.txt. Cause: "
            );
        }
    }

    /**
     * Return the path to the file.
     *
     * @return Path to the file as a string.
     */
    public String getPath() {
        return this.path;
    }
}
