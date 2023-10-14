package cypher.enforcers.code.readers;

import cypher.enforcers.code.readers.types.ReaderType;

/**
 * This class is responsible for returning a CodeReader
 * needed for a specific user.
 */

public class CodeReaderFactory {

    /**
     * Return a CodeReader for a specific structure of a
     * text file based on the social media platform. So
     * far, this supports Google, GitHub, Discord and Shopify.
     * If the readerType is not supported, method returns null.
     *
     * @param readerType The social media platform to specify
     *                   the reader typed needed for the structure
     *                   of the text file.
     * @return A CodeReader object designed to extract codes
     * from that structure of a text file depending on the
     * reader type parameter.
     */
    public static CodeReader makeCodeReader(ReaderType readerType) {
        if (readerType == null) {
            return null;
        }

        // Give correct reader based on the type.
        return switch (readerType) {
            case DISCORD -> new DiscordReader();
            case GITHUB -> new GitHubReader();
            case GOOGLE -> new GoogleReader();
            case SHOPIFY -> new ShopifyReader();
        };
    }

}
