package code.readers;

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
    public static CodeReader makeCodeReader(String readerType) {
        //Clean input.
        readerType = readerType.strip();

        // Give correct reader based on the type.
        if (readerType.equalsIgnoreCase("github")) {
            return new GitHubReader();
        } else if (readerType.equalsIgnoreCase("google")) {
            return new GoogleReader();
        } else if (readerType.equalsIgnoreCase("discord")) {
            return new DiscordReader();
        } else if (readerType.equalsIgnoreCase("shopify")) {
            return new ShopifyReader();
        }

        // Not a valid type.
        return null;
    }

}
