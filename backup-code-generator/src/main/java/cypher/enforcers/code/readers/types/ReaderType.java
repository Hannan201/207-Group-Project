package cypher.enforcers.code.readers.types;

/**
 The different social media platforms that support their backup codes
 being imported through a text file.
 */
public enum ReaderType {

    /** To read backup codes provided by Discord. */
    DISCORD,

    /** To read backup codes provided by GitHub. */
    GITHUB,

    /** To read backup codes provided by Google. */
    GOOGLE,

    /** To read backup codes provided by Shopify. */
    SHOPIFY
}
