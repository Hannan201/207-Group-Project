package cypher.enforcers;

import cypher.enforcers.utilities.Utilities;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public enum Samples {

    DISCORD("discord"),
    GITHUB("github"),
    GOOGLE("google"),
    SHOPIFY("shopify");

    private final String path;

    Samples(String name) {
        try {
            this.path = Paths.get(
                    Utilities.loadFileByURL(
                            "/cypher/enforcers/Sample Text Files/" + name + "_codes_sample.txt"
                    ).toURI()
            ).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() {
        return this.path;
    }
}
